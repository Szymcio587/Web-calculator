context("Menu contents test", () => {
  beforeEach(() => {
    cy.visit("http://localhost:4200");
  })

  it(("Check if h1 header is present"), () => {
    cy.get('h1').should('exist');
  })

  it(("Check options titles"), () => {
    cy.get('.box').first().find('.title').should('contain.text', 'Interpolacja');
    cy.get('.box').eq(1).find('.title').should('contain.text', 'Całkowanie');
  })

  it(("Check boxes contains"), () => {
    cy.get('.box').first().within(() => {
      cy.get('.drop').should('exist')
      cy.get('.plus').should('exist')
      cy.get('.title').should('exist')
      cy.get('.fa-plus').should('exist')
    })
  })
})
