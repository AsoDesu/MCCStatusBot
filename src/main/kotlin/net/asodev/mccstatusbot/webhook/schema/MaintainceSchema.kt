package net.asodev.mccstatusbot.webhook.schema

import com.fasterxml.jackson.annotation.JsonProperty

// Maintaince Impacts
const val IMPACT_PLANNED = "Planned"
const val IMPACT_IN_PROGRESS = "In progress"
const val IMPACT_COMPLETED = "Completed"

class MaintenanceData(
    val backfilled: Boolean,
    val duration: Number,
    val maintenance_start_date: String,
    val maintenance_end_date: String,
    val created_at: String,
    override val impact: String,
    override val name: String,
    val status: String,
    val updated_at: String,
    override val id: String,
    @JsonProperty("maintenance_updates") override val updates: List<StatusUpdate>,
    override val affected_components: List<AffectedComponent>
) : WebhookData()