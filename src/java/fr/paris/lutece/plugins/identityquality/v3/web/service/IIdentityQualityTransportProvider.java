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
package fr.paris.lutece.plugins.identityquality.v3.web.service;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentityExcludeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentityExcludeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.SuspiciousIdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.duplicate.DuplicateRuleSummarySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.DuplicateSearchResponse;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;

/**
 * Interface for providing identity quality transport.
 */
public interface IIdentityQualityTransportProvider
{

    /**
     * Get full list of duplicate rules.
     *
     * @param strApplicationCode
     *            - the application code
     * @return DuplicateRuleSummarySearchResponse containing a list of <code>DuplicateRuleSummaryDto</code>.
     * @throws IdentityStoreException
     */
    DuplicateRuleSummarySearchResponse getAllDuplicateRules( final String strApplicationCode ) throws IdentityStoreException;

    /**
     * Get full list of suspicious identities
     * 
     * @param max
     *            maximum number of results
     * @param page
     *            page to return
     * @param size
     *            number of results per page
     * @return SuspiciousIdentitySearchResponse containing a list of SuspiciousIdentityDto
     */
    SuspiciousIdentitySearchResponse getAllSuspiciousIdentities( final int max, final Integer page, final Integer size ) throws IdentityStoreException;

    /**
     * Get list of suspicious identities for a given rule ID
     * 
     * @param ruleId
     *            the duplicate rule ID
     * @param max
     *            maximum number of results
     * @param page
     *            page to return
     * @param size
     *            number of results per page
     * @return SuspiciousIdentitySearchResponse containing a list of SuspiciousIdentityDto
     */
    SuspiciousIdentitySearchResponse getSuspiciousIdentities( final int ruleId, final int max, final Integer page, final Integer size )
            throws IdentityStoreException;

    /**
     * Get list of identities that are duplicates of the provided customerId's identity, according to the provided rule ID.
     * 
     * @param customerId
     *            the customer ID of the identity
     * @param ruleId
     *            the rule ID
     * @param strApplicationCode
     *            the application code
     * @param max
     *            maximum number of results
     * @param page
     *            page to return
     * @param size
     *            number of results per page
     * @return DuplicateSearchResponse containing a list of {@link fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.QualifiedIdentity}
     */
    DuplicateSearchResponse getDuplicates( final String customerId, final int ruleId, final String strApplicationCode, final int max, final Integer page,
            final Integer size ) throws IdentityStoreException;

    /**
     * Exclude identities from duplicate suspicions.
     * 
     * @param request
     *            a valid SuspiciousIdentityExcludeRequest
     * @param strApplicationCode
     *            the application code
     * @return SuspiciousIdentityExcludeResponse containing the status of the exclusion
     */
    SuspiciousIdentityExcludeResponse excludeIdentities( final SuspiciousIdentityExcludeRequest request, final String strApplicationCode )
            throws IdentityStoreException;
}
