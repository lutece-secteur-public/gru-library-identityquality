/*
 * Copyright (c) 2002-2023, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.identityquality.v3.web.rs.service;

import fr.paris.lutece.plugins.identityquality.v3.web.service.IHttpTransportProvider;
import fr.paris.lutece.plugins.identityquality.v3.web.service.IIdentityQualityTransportProvider;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.IdentityRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.SuspiciousIdentityRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.*;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.duplicate.DuplicateRuleSummarySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.lock.SuspiciousIdentityLockRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.lock.SuspiciousIdentityLockResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.DuplicateSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * IdentityQualityRestClientService
 */
public class IdentityQualityTransportRest extends AbstractTransportRest implements IIdentityQualityTransportProvider
{

    /**
     * Logger
     */
    private static Logger _logger = Logger.getLogger( IdentityQualityTransportRest.class );

    /** URL for identityStore Quality REST service */
    private String _strIdentityStoreQualityEndPoint;

    /**
     * Simple Constructor
     */
    public IdentityQualityTransportRest( )
    {
        super( new HttpAccessTransport( ) );
    }

    /**
     * Constructor with IHttpTransportProvider parameter
     *
     * @param httpTransport
     *            the provider to use
     */
    public IdentityQualityTransportRest( final IHttpTransportProvider httpTransport )
    {
        super( httpTransport );

        _strIdentityStoreQualityEndPoint = httpTransport.getApiEndPointUrl( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DuplicateRuleSummarySearchResponse getAllDuplicateRules( final String strClientCode, final Integer priority ) throws IdentityStoreException
    {
        _logger.debug( "Get duplicate rules of " + strClientCode );

        IdentityRequestValidator.instance( ).checkClientApplication( strClientCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );

        final Map<String, String> mapParams = new HashMap<>( );
        if ( priority != null )
        {
            mapParams.put( Constants.PARAM_RULE_PRIORITY, priority.toString( ) );
        }

        final DuplicateRuleSummarySearchResponse response = _httpTransport.doGet(
                _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.RULES_PATH, mapParams,
                mapHeadersRequest, DuplicateRuleSummarySearchResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SuspiciousIdentityChangeResponse createSuspiciousIdentity( SuspiciousIdentityChangeRequest request, String strClientAppCode )
            throws IdentityStoreException
    {
        _logger.debug( "Create suspicious identity [cuid=" + request.getSuspiciousIdentity( ).getCustomerId( ) + "]" );
        SuspiciousIdentityRequestValidator.instance( ).checkClientApplication( strClientAppCode );
        SuspiciousIdentityRequestValidator.instance( ).checkSuspiciousIdentityChange( request );
        SuspiciousIdentityRequestValidator.instance( ).checkCustomerId( request.getSuspiciousIdentity( ).getCustomerId( ) );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        final Map<String, String> mapParams = new HashMap<>( );

        final SuspiciousIdentityChangeResponse response = _httpTransport.doPostJSON(
                _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.SUSPICIONS_PATH, mapParams,
                mapHeadersRequest, request, SuspiciousIdentityChangeResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SuspiciousIdentitySearchResponse getSuspiciousIdentities( final SuspiciousIdentitySearchRequest request, final String strClientAppCode,
            final int max, final Integer page, final Integer size ) throws IdentityStoreException
    {
        SuspiciousIdentityRequestValidator.instance( ).checkClientApplication( strClientAppCode );
        SuspiciousIdentityRequestValidator.instance( ).checkSuspiciousIdentitySearch( request );

        _logger.debug( "Get all suspicious identities [ruleCode="
                + request.getRuleCode( ) + "][attributes=[ " + request.getAttributes( ).stream( )
                        .map( a -> "[key=" + a.getKey( ) + "][value=" + a.getValue( ) + "]" ).collect( Collectors.joining( " ],[ " ) )
                + "]][max=" + max + "][page=" + page + "][size=" + size + "]" );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientAppCode );

        final Map<String, String> mapParams = new HashMap<>( );
        mapParams.put( Constants.PARAM_MAX, String.valueOf( max ) );
        if ( page != null )
        {
            mapParams.put( Constants.PARAM_PAGE, page.toString( ) );
        }
        if ( size != null )
        {
            mapParams.put( Constants.PARAM_SIZE, size.toString( ) );
        }

        final SuspiciousIdentitySearchResponse response = _httpTransport.doPostJSON( _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3
                + Constants.QUALITY_PATH + "/" + Constants.SUSPICIONS_PATH + Constants.SEARCH_IDENTITIES_PATH, mapParams, mapHeadersRequest, request,
                SuspiciousIdentitySearchResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DuplicateSearchResponse getDuplicates( final String customerId, final String ruleCode, final String strApplicationCode, final int max,
            final Integer page, final Integer size ) throws IdentityStoreException
    {
        _logger.debug( "Get all duplicates for identity [customerId=" + customerId + "[ruleCode=" + ruleCode + "][max=" + max + "][page=" + page + "][size="
                + size + "]" );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strApplicationCode );
        final Map<String, String> mapParams = new HashMap<>( );
        mapParams.put( Constants.PARAM_MAX, String.valueOf( max ) );
        if ( page != null )
        {
            mapParams.put( Constants.PARAM_PAGE, page.toString( ) );
        }
        if ( size != null )
        {
            mapParams.put( Constants.PARAM_SIZE, size.toString( ) );
        }

        return _httpTransport.doGet( _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.DUPLICATE_PATH
                + "/" + customerId + "?" + Constants.PARAM_RULE_CODE + "=" + ruleCode, mapParams, mapHeadersRequest, DuplicateSearchResponse.class, _mapper );
    }

    @Override
    public SuspiciousIdentityExcludeResponse excludeIdentities( final SuspiciousIdentityExcludeRequest request, final String strApplicationCode )
            throws IdentityStoreException
    {
        SuspiciousIdentityRequestValidator.instance( ).checkOrigin( request.getOrigin( ) );
        _logger.debug( "Exclude identities [cuid1=" + request.getIdentityCuid1( ) + "] and [cuid2=" + request.getIdentityCuid2( ) );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strApplicationCode );
        final Map<String, String> mapParams = new HashMap<>( );

        return _httpTransport.doPutJSON( _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.EXCLUSION_PATH,
                mapParams, mapHeadersRequest, request, SuspiciousIdentityExcludeResponse.class, _mapper );
    }

    @Override
    public SuspiciousIdentityExcludeResponse cancelIdentitiesExclusion( final SuspiciousIdentityExcludeRequest request, final String strApplicationCode )
            throws IdentityStoreException
    {
        SuspiciousIdentityRequestValidator.instance( ).checkOrigin( request.getOrigin( ) );
        _logger.debug( "Exclude identities [cuid1=" + request.getIdentityCuid1( ) + "] and [cuid2=" + request.getIdentityCuid2( ) );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strApplicationCode );
        final Map<String, String> mapParams = new HashMap<>( );

        return _httpTransport.doPostJSON(
                _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.EXCLUSION_PATH, mapParams,
                mapHeadersRequest, request, SuspiciousIdentityExcludeResponse.class, _mapper );
    }

    @Override
    public SuspiciousIdentityLockResponse lock( final SuspiciousIdentityLockRequest request, final String strClientCode ) throws IdentityStoreException
    {
        SuspiciousIdentityRequestValidator.instance( ).checkClientApplication( strClientCode );
        _logger.debug( "Manage lock identity [cuid=" + request.getCustomerId( ) + "] with [locked=" + request.isLocked( ) + "]" );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        final Map<String, String> mapParams = new HashMap<>( );

        return _httpTransport.doPostJSON( _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.LOCK_PATH,
                mapParams, mapHeadersRequest, request, SuspiciousIdentityLockResponse.class, _mapper );
    }
}
