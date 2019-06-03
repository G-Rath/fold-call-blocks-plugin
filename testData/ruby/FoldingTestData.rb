# @formatter:off
require "rails_helper"

RSpec.describe 'root block' <fold text='do ... end'>do
  <fold text='1'>describe '1' <fold text='do ... end'>do
    <fold text='1.1'>it '1.1' <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
    <fold text='1.2'>it '1.2' <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
    <fold text='1.3'>it "1.3" <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
  end</fold></fold>
  <fold text='2'>describe('2') <fold text='{ ... }'>{
    <fold text='2.1'>it '2.1' <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
    <fold text='2.2'>it('2.2') <fold text='{ ... }'>{
      expect(Devise.expire_password_after).to eq 90.days
    }</fold></fold>
    <fold text='2.3'>it '2.3' <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
  }</fold></fold>
  <fold text='3'>describe "3" <fold text='do ... end'>do
    <fold text='3.1'>it("3.1") <fold text='{ ... }'>{
      expect(Devise.expire_password_after).to eq 90.days
    }</fold></fold>
    <fold text='3.2'>it '3.2' <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
    <fold text='3.3'>it "3.3" <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
  end</fold></fold>
  <fold text='1'>describe '1' <fold text='do ... end'>do
    <fold text='1.1'>it '1.1' <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold>
</fold>
    <fold text='1.2'>it '1.2' <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold>

</fold>
    <fold text='1.3'>it "1.3" <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
  end</fold></fold>
  <fold text='2'>describe('2') <fold text='{ ... }'>{
    <fold text='2.1'>it '2.1' <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
    <fold text='2.2'>it('2.2') <fold text='{ ... }'>{
      expect(Devise.expire_password_after).to eq 90.days
    }</fold>

</fold>
    <fold text='2.3'>it '2.3' <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
  }</fold></fold>
  <fold text='3'>describe "3" <fold text='do ... end'>do
    <fold text='3.1'>it("3.1") <fold text='{ ... }'>{
      expect(Devise.expire_password_after).to eq 90.days
    }</fold></fold>
    <fold text='3.2'>it '3.2' <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold>

  </fold>
    <fold text='3.3'>it "3.3" <fold text='do ... end'>do
      expect(Devise.expire_password_after).to eq 90.days
    end</fold></fold>
  end</fold></fold>
end</fold>
