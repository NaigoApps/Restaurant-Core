export default class RenderingData{
    constructor(text, bgColor){
        this._text = text;
        this._bgColor = bgColor;
    }


    get text() {
        return this._text;
    }

    set text(value) {
        this._text = value;
    }

    get backgroundColor() {
        return this._bgColor;
    }

    set backgroundColor(value) {
        this._bgColor = value;
    }
}