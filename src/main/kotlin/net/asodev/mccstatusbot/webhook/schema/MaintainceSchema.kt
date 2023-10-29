package net.asodev.mccstatusbot.webhook.schema

class MaintenanceData(
    val backfilled: Boolean,
    val duration: Number,
    val maintenance_start_date: String,
    val maintenance_end_date: String,
    val created_at: String,
    override val impact: String,
    val name: String,
    val resolved_at: String,
    val status: String,
    val updated_at: String,
    val id: String,
    val maintenance_updates: List<StatusUpdate>,
    override val affected_components: List<AffectedComponent>
) : WebhookData()