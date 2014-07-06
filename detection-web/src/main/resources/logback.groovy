import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.INFO

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d %5p | %15t | %-40logger{40} | %m %n"
    }
}

logger("pl.info.rkluszczynski.image", INFO)
logger("org.springframework", INFO)

root(INFO, ["CONSOLE"])
