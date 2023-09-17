package net.asodev.mccstatusbot.webhook.schema

// Statuses
const val STATUS_OPERATIONAL = "Operational"
const val STATUS_UNDER_MAINTENANCE = "Under maintenance"
const val STATUS_DEGRADED_PERFORMANCE = "Degraded performance"
const val STATUS_PARTIAL_OUTAGE = "Partial outage"
const val STATUS_MAJOR_OUTAGE = "Major outage"

class IncidentData(
    val backfilled: Boolean,
    val created_at: String,
    val impact: String,
    val name: String,
    val status: String,
    val id: String,
    val incident_updates: List<IncidentUpdate>,
    val url: String,
    val affected_components: List<AffectedComponent>
)

class IncidentUpdate(
    val id: String,
    val incident_id: String,
    val body: String,
    val markdown: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
)

class AffectedComponent(
    val id: String,
    val name: String,
    val status: String
)