package com.folkmanis.laiks.utilities.ext

import java.time.LocalTime
import java.time.format.DateTimeFormatter

val LocalTime.minutesString: String
    get() = this.format(DateTimeFormatter.ofPattern("mm"))

val LocalTime.hoursString: String
    get() = this.format(DateTimeFormatter.ofPattern("H"))
