![MCC Status](https://i.imgur.com/Da6bv1C_d.webp?maxwidth=600&fidelity=grand)

---

An automated Twitter bot for posting data from the [MCC Status Page](https://status.mcchampionship.com/)!

### How it works

MCC uses [Instatus](https://instatus.com/home) for their status page, which also offers a
**Webhook API**. This app essentially just listens for those webhook updates, formats them
nicely and posts them to the [Twitter API](https://developer.twitter.com/en/docs/twitter-api)
