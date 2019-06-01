// @formatter:off
describe('root', () => {
  describe('single quotes', () => {
    it('is true 1', () => {
      expect(true).toBe(true);
    });
    it('is true 2', () => {
      expect(true).toBe(true);
    });
    it('is true 3', () => {
      expect(true).toBe(true);
    });
  });
  describe("double quotes", () => {
    it("is true 1", () => {
      expect(true).toBe(true);
    });
    it("is true 2", () => {
      expect(true).toBe(true);
    });
    it("is true 3", () => {
      expect(true).toBe(true);
    });
  });
  describe(`simple template literal`, () => {
    it(`is true 1`, () => {
      expect(true).toBe(true);
    });
    it(`is true 2`, () => {
      expect(true).toBe(true);
    });
    it(`is true 3`, () => {
      expect(true).toBe(true);
    });
  });
  describe(`complex template ${'literal'}`, () => {
    it(`is true ${'1'}`, () => {
      expect(true).toBe(true);
    });
    it(`is true ${"2"}`, () => {
      expect(true).toBe(true);
    });
    it(`is ${true} 3`, () => {
      expect(true).toBe(true);
    });
  });
  describe('mixed strings', () => {
    it('is true 1', () => {
      expect(true).toBe(true);
    });
    it("is true 2", () => {
      expect(true).toBe(true);
    });
    it(`is ${true} 3`, () => {
      expect(true).toBe(true);
    });
  });
  describe('new lines', () => {
    it('is true 1', () => {
      expect(true).toBe(true);
    });

    it("is true 2", () => {
      expect(true).toBe(true);
    });


    it(`is ${true} 3`, () => {
      expect(true).toBe(true);
    });

  });
});
