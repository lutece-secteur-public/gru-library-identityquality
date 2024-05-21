/*
 * Copyright (c) 2002-2024, City of Paris
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
import fr.paris.lutece.plugins.identitystore.v3.web.rs.SuspiciousIdentityRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentityExcludeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentityExcludeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.duplicate.DuplicateRuleSummarySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.lock.SuspiciousIdentityLockRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.lock.SuspiciousIdentityLockResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.DuplicateSearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.DuplicateSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;

import java.util.HashMap;
import java.util.Map;

/**
 * IdentityQualityRestClientService
 */
public class IdentityQualityTransportRest extends AbstractTransportRest implements IIdentityQualityTransportProvider
{
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
    public DuplicateRuleSummarySearchResponse getAllDuplicateRules( final String strClientCode, final RequestAuthor author, final Integer priority )
            throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final Map<String, String> mapParams = new HashMap<>( );
        if ( priority != null )
        {
            mapParams.put( Constants.PARAM_RULE_PRIORITY, priority.toString( ) );
        }

        final String url = _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.RULES_PATH;
        return _httpTransport.doGet( url, mapParams, mapHeadersRequest, DuplicateRuleSummarySearchResponse.class, _mapper );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SuspiciousIdentityChangeResponse createSuspiciousIdentity( final SuspiciousIdentityChangeRequest request, final String strClientCode,
            final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );
        SuspiciousIdentityRequestValidator.instance( ).checkSuspiciousIdentityChange( request );
        SuspiciousIdentityRequestValidator.instance( ).checkCustomerId( request.getSuspiciousIdentity( ).getCustomerId( ) );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final Map<String, String> mapParams = new HashMap<>( );

        final String url = _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.SUSPICIONS_PATH;
        return _httpTransport.doPostJSON( url, mapParams, mapHeadersRequest, request, SuspiciousIdentityChangeResponse.class, _mapper );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SuspiciousIdentitySearchResponse getSuspiciousIdentities( final SuspiciousIdentitySearchRequest request, final String strClientCode,
            final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );
        SuspiciousIdentityRequestValidator.instance( ).checkSuspiciousIdentitySearch( request );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final Map<String, String> mapParams = new HashMap<>( );

        final String url = _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.SUSPICIONS_PATH
                + Constants.SEARCH_IDENTITIES_PATH;
        return _httpTransport.doPostJSON( url, mapParams, mapHeadersRequest, request, SuspiciousIdentitySearchResponse.class, _mapper );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DuplicateSearchResponse getDuplicates( final String customerId, final String ruleCode, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );
        SuspiciousIdentityRequestValidator.instance( ).checkCustomerId( customerId );
        SuspiciousIdentityRequestValidator.instance( ).checkRuleCode( ruleCode );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final HashMap<String, String> mapParams = new HashMap<>( );
        mapParams.put( Constants.PARAM_RULE_CODE, ruleCode );

        final String url = _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.DUPLICATE_PATH + "/"
                + customerId;
        return _httpTransport.doGet( url, mapParams, mapHeadersRequest, DuplicateSearchResponse.class, _mapper );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DuplicateSearchResponse searchDuplicates( final DuplicateSearchRequest request, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );
        SuspiciousIdentityRequestValidator.instance( ).checkDuplicateSearch( request );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.DUPLICATE_PATH
                + Constants.SEARCH_IDENTITIES_PATH;
        return _httpTransport.doPostJSON( url, new HashMap<>( ), mapHeadersRequest, request, DuplicateSearchResponse.class, _mapper );
    }

    @Override
    public SuspiciousIdentityExcludeResponse excludeIdentities( final SuspiciousIdentityExcludeRequest request, final String strClientCode,
            final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );
        SuspiciousIdentityRequestValidator.instance( ).checkSuspiciousIdentityExclude( request );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + Constants.EXCLUSION_PATH;
        return _httpTransport.doPutJSON( url, null, mapHeadersRequest, request, SuspiciousIdentityExcludeResponse.class, _mapper );
    }

    @Override
    public SuspiciousIdentityExcludeResponse cancelIdentitiesExclusion( final SuspiciousIdentityExcludeRequest request, final String strClientCode,
            final RequestAuthor author ) throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );
        SuspiciousIdentityRequestValidator.instance( ).checkSuspiciousIdentityExclude( request );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + Constants.CANCEL_IDENTITIES_EXCLUSION_PATH;
        return _httpTransport.doPostJSON( url, null, mapHeadersRequest, request, SuspiciousIdentityExcludeResponse.class, _mapper );
    }

    @Override
    public SuspiciousIdentityLockResponse lock( final SuspiciousIdentityLockRequest request, final String strClientCode, final RequestAuthor author )
            throws IdentityStoreException
    {
        this.checkCommonHeaders( strClientCode, author );
        SuspiciousIdentityRequestValidator.instance( ).checkLockRequest( request );

        final Map<String, String> mapHeadersRequest = new HashMap<>( );
        mapHeadersRequest.put( Constants.PARAM_CLIENT_CODE, strClientCode );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_NAME, author.getName( ) );
        mapHeadersRequest.put( Constants.PARAM_AUTHOR_TYPE, author.getType( ).name( ) );

        final String url = _strIdentityStoreQualityEndPoint + Constants.VERSION_PATH_V3 + Constants.QUALITY_PATH + "/" + Constants.LOCK_PATH;
        return _httpTransport.doPostJSON( url, null, mapHeadersRequest, request, SuspiciousIdentityLockResponse.class, _mapper );
    }
}
