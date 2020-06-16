package com.example.mypainting;

public enum DrawStrokeEnum {
     LEVEL1(4,30),
     LEVEL2(8,50),
     LEVEL3(15,70);
    private int penStroke;
    private int eraserStroke;

    DrawStrokeEnum(int penStroke, int eraserStroke) {
        this.penStroke = penStroke;
        this.eraserStroke = eraserStroke;
    }

    public int getEraserStroke() {
        return eraserStroke;
    }

    public int getPenStroke() {
        return penStroke;
    }
}
