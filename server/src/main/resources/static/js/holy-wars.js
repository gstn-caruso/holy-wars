document.addEventListener("DOMContentLoaded", () => {
    const eventsSource = document.querySelector("[data-town-events]");
    if (!eventsSource) {
        return;
    }
    const events = new EventSource(eventsSource.dataset.townEvents);
    ["town", "resources"].forEach((eventName) => {
        events.addEventListener(eventName, () => {
            document.body.dispatchEvent(new CustomEvent(eventName));
        });
    });
});
