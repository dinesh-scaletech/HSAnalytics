package com.scaletech.hsanalytic.utils

/**
 *  Unique clickid that is pass via referral to google plat store
 *  Date type: String
 *  Required: true
 */
const val PARAM_CLICK_ID: String = "clickid"

/**
 *  Secrete to authenticate user
 *  Date type: String
 *  Required: true
 */
const val PARAM_K: String = "k"

/**
 * Optional id of app owner
 * Date type: String
 * Required: no
 */
const val PARAM_TRANSACTION_ID: String = "transactionid"

/**
 * Specifies what action the user performed. System will default to 0 if nothing is being passed.
 * Installs should either omit this parameter or pass 0. For non install events, app owner should be able to chose the value
 * Date type: String
 * Required: no
 */
const val PARAM_EVENT_ID: String = "eventid"

/**
 * Monitoring value that should assigned to this conversion.
 * Date type: Decimal
 * Required: no
 */

const val PARAM_RECEIVE: String = "receive"

/**
 * Either 0 or 1, if 1 duplicate post backs with the same clickid and eventid are allowed.
 * Otherwise they are rejected. Default is 0
 * Date type: Integer
 * Required: no
 */
const val PARAM_DUPLICATE: String = "duplicate"

/**
 * Either 0 or 1, if 1 post backs will be queued and not handled in real time.
 * Default is 0
 * Date type: Integer
 * Required: no
 */
const val PARAM_B: String = "b"

/**
 *Additional details
 * Date type: String (JSON)
 * Required: no
 */
const val PARAM_DETAILS: String = "details"





