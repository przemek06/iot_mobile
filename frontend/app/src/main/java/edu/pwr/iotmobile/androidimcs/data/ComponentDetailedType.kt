package edu.pwr.iotmobile.androidimcs.data

enum class ComponentDetailedType(
    val belongsTo: ComponentType,
    val size: Int
) {
    Button(ComponentType.INPUT, 1),
    Toggle(ComponentType.INPUT, 1),
    Slider(ComponentType.INPUT, 2),
    Discord(ComponentType.TRIGGER, 1),
    Email(ComponentType.TRIGGER, 1),
    Slack(ComponentType.TRIGGER, 1),
    Telegram(ComponentType.TRIGGER, 1),
    LineGraph(ComponentType.OUTPUT, 2),
}