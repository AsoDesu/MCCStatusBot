package net.asodev.mccstatusbot.webhook.schema

import com.fasterxml.jackson.annotation.JsonProperty

// Statuses
const val STATUS_OPERATIONAL = "Operational"
const val STATUS_UNDER_MAINTENANCE = "Under maintenance"
const val STATUS_DEGRADED_PERFORMANCE = "Degraded performance"
const val STATUS_PARTIAL_OUTAGE = "Partial outage"
const val STATUS_MAJOR_OUTAGE = "Major outage"

// Incident Impacts
const val IMPACT_INVESTIGATING = "Investigating"
const val IMPACT_IDENTIFIED = "Identified"
const val IMPACT_MONITORING = "Monitoring"
const val IMPACT_RESOLVED = "Resolved"

class IncidentData(
    val backfilled: Boolean,
    val created_at: String,
    override val impact: String,
    override val name: String,
    val status: String,
    override val id: String,
    @JsonProperty("incident_updates") override val updates: List<StatusUpdate>,
    val url: String,
    override val affected_components: List<AffectedComponent>
) : WebhookData()

class StatusUpdate(
    val id: String,
    val body: String,
    val markdown: String,
    val created_at: String,
    val updated_at: String,
)

class AffectedComponent(
    val id: String,
    val name: String,
    val status: String
)