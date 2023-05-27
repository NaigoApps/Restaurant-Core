export default class Color {
  constructor(rgb) {
    this.r = rgb.r;
    this.g = rgb.g;
    this.b = rgb.b;
    const hsl = Color.RGB2HSL(rgb.r, rgb.g, rgb.b);
    [this.h, this.s, this.l] = hsl;
  }

  get red() {
    return this.r;
  }

  get green() {
    return this.g;
  }

  get blue() {
    return this.b;
  }

  get hue() {
    return this.h;
  }

  get saturation() {
    return this.s;
  }

  get lightness() {
    return this.l;
  }

  toHexString() {
    return `#${`00${Math.floor(this.red)
      .toString(16)
      .toUpperCase()}`.slice(-2)}${`00${Math.floor(this.green)
      .toString(16)
      .toUpperCase()}`.slice(-2)}${`00${Math.floor(this.blue)
      .toString(16)
      .toUpperCase()}`.slice(-2)}`;
  }

  toRGBInt() {
    return Math.floor(this.red) * 256 * 256 + Math.floor(this.green) * 256 + Math.floor(this.blue);
  }

  foreground() {
    if (this.lightness > 0.3) {
      return Color.black;
    }
    return Color.white;
  }

  static fromHexString(str) {
    return this.fromRGBInt(parseInt(`0x${str.substring(1)}`, 10));
  }

  static fromRGBInt(rgb) {
    return this.fromRGB(rgb / 256 / 256, (rgb / 256) % 256, rgb % 256);
  }

  static fromRGB(r, g, b) {
    return new Color({ r, g, b });
  }

  static fromHSL(h, s, l) {
    return Color.fromRGB(...Color.HSL2RGB(h, s, l));
  }

  static RGB2HSL(r, g, b) {
    const nR = r / 255;
    const nG = g / 255;
    const nB = b / 255;

    const max = Math.max(nR, nG, nB);
    const min = Math.min(nR, nG, nB);
    let h;
    let s;
    const l = (max + min) / 2;

    if (max === min) {
      h = 0;
      s = 0;
    } else {
      const d = max - min;
      s = l > 0.5 ? d / (2 - max - min) : d / (max + min);

      switch (max) {
        case nR:
          h = (nG - nB) / d + (nG < nB ? 6 : 0);
          break;
        case nG:
          h = (nB - nR) / d + 2;
          break;
        case nB:
          h = (nR - nG) / d + 4;
          break;
        default:
          break;
      }

      h /= 6;
    }

    return [h, s, l];
  }

  static HSL2RGB(h, s, l) {
    let r;
    let g;
    let b;

    if (s === 0) {
      r = l;
      g = l;
      b = l;
    } else {
      const q = l < 0.5 ? l * (1 + s) : l + s - l * s;
      const p = 2 * l - q;

      r = this.HUE2RGB(p, q, h + 1 / 3);
      g = this.HUE2RGB(p, q, h);
      b = this.HUE2RGB(p, q, h - 1 / 3);
    }

    return [r * 255, g * 255, b * 255];
  }

  static HUE2RGB(p, q, t) {
    let nT = t;
    if (nT < 0) nT += 1;
    if (nT > 1) nT -= 1;
    if (nT < 1 / 6) return p + (q - p) * 6 * nT;
    if (nT < 1 / 2) return q;
    if (nT < 2 / 3) return p + (q - p) * (2 / 3 - nT) * 6;
    return p;
  }

  static get black() {
    return Color.fromHexString('#000000');
  }

  static get white() {
    return Color.fromHexString('#FFFFFF');
  }

  static get primary() {
    return Color.fromHexString('#0275d8');
  }

  static get secondary() {
    return Color.fromHexString('#FFFFFF');
  }

  static get success() {
    return Color.fromHexString('#5cb85c');
  }

  static get danger() {
    return Color.fromHexString('#d9534f');
  }

  static get warning() {
    return Color.fromHexString('#f0ad4e');
  }

  static get info() {
    return Color.fromHexString('#5bc0de');
  }

  get darker() {
    const l = this.lightness / 2;
    return Color.fromHSL(this.hue, this.saturation, l);
  }
}
