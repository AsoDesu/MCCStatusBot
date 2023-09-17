package net.asodev.mccstatusbot.webhook

import net.asodev.mccstatusbot.twitter.TweetRequest
import net.asodev.mccstatusbot.twitter.Twitter
import net.asodev.mccstatusbot.webhook.schema.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date

@RestController
class InstatusWebhook(val twitter: Twitter) {

    // Status to be shown as "<Component> experiencing <Status>"
    val EXPERIENCING = listOf(STATUS_MAJOR_OUTAGE, STATUS_PARTIAL_OUTAGE, STATUS_DEGRADED_PERFORMANCE)
    // Status to be shown as "<Component> is <Status>"
    val IS = listOf(STATUS_UNDER_MAINTENANCE, STATUS_OPERATIONAL)

    @PostMapping("/webhook")
    fun webhook(@RequestBody data: InstatusUpdate) {
        val incident = data.incident ?: return

        val content = buildString {
            val component = incident.affected_components.firstOrNull()
            if (component != null) {
                this.appendLine( getComponentStatusMessage(component) )
                this.appendLine()
            }
            this.appendLine("${incident.impact} - ${incident.name}")

            val updates = incident.incident_updates.sortedBy { Instant.from(DateTimeFormatter.ISO_INSTANT.parse(it.updated_at)).epochSecond }
            val description = updates.lastOrNull()
            if (description != null) {
                this.appendLine(description.markdown)
            }
        }

        val tweet = TweetRequest(
            text = content
        )
        twitter.sendTweet(tweet)
    }

    fun getComponentStatusMessage(component: AffectedComponent): String {
        val name = replaceComponentName(component.name)
        return if (EXPERIENCING.contains(component.status)) {
             "$name experiencing ${component.status}"
        } else if (IS.contains(component.status)) {
            "$name is ${component.status}"
        } else ""
    }

    fun replaceComponentName(name: String): String {
        return when(name) {
            "MCC Island Minecraft Server" -> "MCC Island"
            else -> name
        }
    }

}