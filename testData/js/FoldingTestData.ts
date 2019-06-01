// @formatter:off
describe('root', () => <fold text='{...}'>{
  <fold text='when something happens'>describe('when something happens', () => <fold text='{...}'>{
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
}</fold>);
