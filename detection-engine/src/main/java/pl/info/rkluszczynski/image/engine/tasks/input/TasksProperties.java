package pl.info.rkluszczynski.image.engine.tasks.input;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TasksProperties {

    @Value("${multistage.scale.depth}")
    private Integer multistageScaleDepth;

    @Value("${multistage.scale.ratio}")
    private Double multistageScaleRatio;


    public Integer getMultistageScaleDepth() {
        return multistageScaleDepth;
    }

    public Double getMultistageScaleRatio() {
        return multistageScaleRatio;
    }
}
