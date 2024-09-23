// Code from Real World App (RWA)
import { slowCypressDown } from 'cypress-slow-down';

slowCypressDown();

describe('Cypress Studio Demo', () => {
  beforeEach(() => {
    // Seed database with test data
    // cy.task('db:seed')

    // Login test user
    // cy.database('find', 'users').then((user) => {
    //   cy.login(user.username, 's3cret', true)
    // })
  });

  it('create new transaction', () => {
    /* ==== Generated with Cypress Studio ==== */
    cy.visit('localhost:4173');
    cy.get('[href="/about"]').click();
    cy.get('[href="/"]').click();
    /* ==== End Cypress Studio ==== */
  });
});
