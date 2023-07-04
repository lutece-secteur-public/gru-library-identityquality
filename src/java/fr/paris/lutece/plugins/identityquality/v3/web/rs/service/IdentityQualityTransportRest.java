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
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentityExcludeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentityExcludeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.duplicate.DuplicateRuleSummarySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.DuplicateSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

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
<<<<<<< HEAD
=======
     * Simple Constructor
     */
    public IdentityQualityTransportRest( )
    {
        super( new HttpAccessTransport( ) );
    }

    /**
>>>>>>> 4bf9a61 (#238 put id-quality services in separate plugin)
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
    public DuplicateRuleSummarySearchResponse getAllDuplicateRules( final String strApplicationCode ) throws IdentityStoreException
    {
        _logger.debug( "Get duplicate rules of " + strApplicationCode );

        this.checkClientCode( strApplicationCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strApplicationCode );

        final Map<String, String> mapParams = new HashMap<>( );

        final DuplicateRuleSummarySearchResponse response = _httpTransport.doGet(
                _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.RULES_PATH, mapParams,
                mapHeadersRequest, DuplicateRuleSummarySearchResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SuspiciousIdentitySearchResponse getAllSuspiciousIdentities( int max, Integer page, Integer size ) throws IdentityStoreException
    {
        _logger.debug( "Get all suspicious identities [max=" + max + "][page=" + page + "][size=" + size + "]" );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
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

        final SuspiciousIdentitySearchResponse response = _httpTransport.doGet(
                _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.SUSPICIONS_PATH, mapParams,
                mapHeadersRequest, SuspiciousIdentitySearchResponse.class, _mapper );

        return response;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SuspiciousIdentitySearchResponse getSuspiciousIdentities( int ruleId, int max, Integer page, Integer size ) throws IdentityStoreException
    {
        _logger.debug( "Get all suspicious identities [ruleId=" + ruleId + "][max=" + max + "][page=" + page + "][size=" + size + "]" );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
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

        final SuspiciousIdentitySearchResponse response = _httpTransport.doGet(
                _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.SUSPICIONS_PATH + "/" + ruleId,
                mapParams, mapHeadersRequest, SuspiciousIdentitySearchResponse.class, _mapper );

        return response;
    }
    
    /**
     * check whether the parameters related to the identity are valid or not
     *
     * @param strClientCode
     *            the strClientCode
     * @throws IdentityStoreException
     *             if the parameters are not valid
     */
    public void checkClientCode( String strClientCode ) throws IdentityStoreException
    {
        if ( StringUtils.isBlank( strClientCode ) )
        {
            throw new IdentityStoreException( "Client code is mandatory." );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DuplicateSearchResponse getDuplicates( final String customerId, final int ruleId, final String strApplicationCode, final int max, final Integer page,
            final Integer size ) throws IdentityStoreException
    {
        _logger.debug( "Get all duplicates for identity [customerId=" + customerId + "[ruleId=" + ruleId + "][max=" + max + "][page=" + page + "][size=" + size
                + "]" );

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

        final DuplicateSearchResponse response = _httpTransport.doGet( _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH
                + "/" + Constants.DUPLICATE_PATH + "/" + customerId + "?rule_id=" + ruleId, mapParams, mapHeadersRequest, DuplicateSearchResponse.class,
                _mapper );
        return response;
    }

    @Override
    public SuspiciousIdentityExcludeResponse excludeIdentities( final SuspiciousIdentityExcludeRequest request, final String strApplicationCode )
            throws IdentityStoreException
    {
        checkExcludeRequest( request );
        _logger.debug( "Exclude identities [cuid1=" + request.getIdentityCuid1( ) + "] and [cuid2=" + request.getIdentityCuid2( ) + "] for [ruleId="
                + request.getRuleId( ) + "]" );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strApplicationCode );
        final Map<String, String> mapParams = new HashMap<>( );

        final SuspiciousIdentityExcludeResponse response = _httpTransport.doPutJSON(
                _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + Constants.EXCLUSION_PATH, mapParams, mapHeadersRequest,
                request, SuspiciousIdentityExcludeResponse.class, _mapper );

        return response;
    }

    /**
     * Check whether the parameters related to the Suspicious Identity Exclude request are valid or not
     *
     * @param request
     *            the Suspicious Identity Exclude request
     * @throws IdentityStoreException
     *             if the parameters are not valid
     */
    private void checkExcludeRequest( final SuspiciousIdentityExcludeRequest request ) throws IdentityStoreException
    {
        if ( request == null || StringUtils.isAnyBlank( request.getIdentityCuid1( ), request.getIdentityCuid2( ) ) || request.getRuleId( ) == null )
        {
            throw new IdentityStoreException( "Provided Suspicious Identity Exclude request is null or not properly filled." );
        }
    }

}
