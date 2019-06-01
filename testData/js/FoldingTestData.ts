// @formatter:off
describe('root', () => <fold text='{...}'>{
  <fold text='single quotes'>describe('single quotes', () => <fold text='{...}'>{
    <fold text='is true 1'>it('is true 1', () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
    <fold text='is true 2'>it('is true 2', () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
    <fold text='is true 3'>it('is true 3', () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
  }</fold>);</fold>
  <fold text='double quotes'>describe("double quotes", () => <fold text='{...}'>{
    <fold text='is true 1'>it("is true 1", () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
    <fold text='is true 2'>it("is true 2", () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
    <fold text='is true 3'>it("is true 3", () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
  }</fold>);</fold>
  <fold text='simple template literal'>describe(`simple template literal`, () => <fold text='{...}'>{
    <fold text='is true 1'>it(`is true 1`, () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
    <fold text='is true 2'>it(`is true 2`, () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
    <fold text='is true 3'>it(`is true 3`, () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
  }</fold>);</fold>
  <fold text='complex template ${`literal`}'>describe(`complex template ${`literal`}`, () => <fold text='{...}'>{
    <fold text='is true ${`1`}'>it(`is true ${`1`}`, () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
    <fold text='is true ${"2"}'>it(`is true ${"2"}`, () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
    <fold text='is ${true} 3'>it(`is ${true} 3`, () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
  }</fold>);</fold>
  <fold text='mixed strings'>describe('mixed strings', () => <fold text='{...}'>{
    <fold text='is true 1'>it('is true 1', () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
    <fold text='is true 2'>it("is true 2", () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
    <fold text='is ${true} 3'>it(`is ${true} 3`, () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>
  }</fold>);</fold>
  <fold text='new lines'>describe('new lines', () => <fold text='{...}'>{
    <fold text='is true 1'>it('is true 1', () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);
</fold>
    <fold text='is true 2'>it("is true 2", () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);

</fold>
    <fold text='is ${true} 3'>it(`is ${true} 3`, () => <fold text='{...}'>{
      expect(true).toBe(true);
    }</fold>);</fold>

  }</fold>);</fold>
}</fold>);
