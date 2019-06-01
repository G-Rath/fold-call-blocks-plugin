require "rails_helper"

RSpec.describe 'root do block' do
  describe '1' do
    it '1.1' do
      expect(Devise.expire_password_after).to eq 90.days
    end
    it '1.2' do
      expect(Devise.expire_password_after).to eq 90.days
    end
    it "1.3" do
      expect(Devise.expire_password_after).to eq 90.days
    end
  end
  describe('2') {
    it '2.1' do
      expect(Devise.expire_password_after).to eq 90.days
    end
    it('2.2') {
      expect(Devise.expire_password_after).to eq 90.days
    }
    it '2.3' do
      expect(Devise.expire_password_after).to eq 90.days
    end
  }
  describe "3" do
    it("3.1") {
      expect(Devise.expire_password_after).to eq 90.days
    }
    it '3.2' do
      expect(Devise.expire_password_after).to eq 90.days
    end
    it "3.3" do
      expect(Devise.expire_password_after).to eq 90.days
    end
  end
end
