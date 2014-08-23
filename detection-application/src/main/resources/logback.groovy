import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.*

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d %5p | %t | %-50logger{50} | %m %n"
    }
}

logger("pl.info.rkluszczynski.image", DEBUG)

logger("org.hibernate", WARN)
logger("org.springframework", WARN)

root(INFO, ["CONSOLE"])
