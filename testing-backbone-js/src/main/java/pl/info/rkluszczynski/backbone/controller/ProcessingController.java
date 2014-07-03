package pl.info.rkluszczynski.backbone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.info.rkluszczynski.backbone.model.ProcessingStatus;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class ProcessingController {

    public static final int MAX_VALUE = 7;
    private final AtomicInteger counter = new AtomicInteger();

    @RequestMapping("/processing")
    public
    @ResponseBody
    ProcessingStatus processing() {
        System.out.println("==== in processing ====");
        int value = counter.incrementAndGet();
        if (value == MAX_VALUE) {
            counter.set(0);
        }
        double ratio = (double) value / (double) MAX_VALUE;
        ratio *= 100;

        return new ProcessingStatus((int) ratio, -1);
    }
}
