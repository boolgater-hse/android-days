package com.studyandroid.musicapp.data.enums

enum class SampleRateEnum(val rate: Double) {
    _44_1(44.1),
    _48(48.0),
    _88_2(88.2),
    _96(96.0),
    _192(192.0);

    companion object {
        private val types = values().associateBy { it.rate }

        fun fromDouble(rate: Double): SampleRateEnum? = types[rate]
    }
}
