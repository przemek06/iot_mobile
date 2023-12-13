package edu.pwr.iotmobile.androidimcs.data

enum class TopicDataType(val isNumeric: Boolean) {
    FLOAT(true),
    INT(true),
    TEXT(false),
    IMAGE(false)
}