package ocrexcel.demo.fuxin.moddle;

import lombok.Data;

@Data
public class PaddleDTO {
    private PaddleNode topLeft;
    private PaddleNode topRight;
    private PaddleNode downLeft;
    private PaddleNode downRight;
    private String words;
    private double reliability;
}
