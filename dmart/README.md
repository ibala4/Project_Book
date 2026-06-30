# FreshMart — Setup

A grocery/retail storefront built with Spring Boot + PostgreSQL. Browse
products by category, search, and add items to a cart (demo only — no real
checkout/payment).

## Required environment variables

| Variable      | Purpose                          |
|---------------|------------------------------------|
| `DB_USERNAME` | Your PostgreSQL username (default: `postgres`) |
| `DB_PASSWORD` | Your PostgreSQL password          |

## Setup

1. **Create the database** in pgAdmin: right-click Databases → Create →
   Database → name it `dmartdb` → Save.

2. **Set environment variables in Eclipse**: Right-click the project →
   Run As → Run Configurations → select `DmartApplication` → Environment
   tab → add `DB_PASSWORD` (and `DB_USERNAME` if not `postgres`).

3. **Run** `DmartApplication.java`. Your browser opens automatically to
   the login page.

4. **Log in** with the seeded test account:
   - Email: `shopper@example.com`
   - Password: `test1234`

   Or create your own account via the "Create one" link.

## What's seeded

15 grocery products across 5 categories (Fruits & Vegetables, Dairy & Eggs,
Snacks, Beverages, Staples), including one out-of-stock item (Biscuits) to
demonstrate the "Unavailable" state.

## Notes

- Cart state lives only in the browser tab's memory — refreshing the page
  clears the cart (no backend cart/order persistence in this version).
- Checkout button is a placeholder — it does not process real payments
  or create real orders.
