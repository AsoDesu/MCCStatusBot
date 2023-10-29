package net.asodev.mccstatusbot.webhook.schema

class InstatusUpdate(
    val meta: WebhookMeta,
    val page: PageData,
    val incident: IncidentData? = null,
    val maintenance: MaintenanceData? = null
)

class WebhookMeta(
    val unsubscribe: String,
    val documentation: String
)
class PageData(
    val id: String,
    val status_indicator: String,
    val status_description: String,
    val url: String
)

abstract class WebhookData {
    abstract val impact: String
    abstract val affected_components: List<AffectedComponent>
}