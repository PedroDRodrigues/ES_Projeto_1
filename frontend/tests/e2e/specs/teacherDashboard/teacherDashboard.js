describe('Teacher Dashboard', () => {

    beforeEach(() => {
        cy.request('http://localhost:8080/auth/demo/teacher')
        .as('loginResponse')
        .then((response) => {
            Cypress.env('token', response.body.token);
            return response;
        });

        cy.demoTeacherLogin();
    });

    /*
     * First test:
     * Test if the values presented on the dashboard from 2023's course execution are correct
     * and verify that 3 bar charts are shown - 2019, 2022 and 2023 course executions.
     */
    it('teacher accesses dashboard of 2023 course execution', () => {
        cy.intercept('GET', '**/teachers/dashboards/executions/*').as(
            'getTeacherDashboard'
        );
        cy.intercept('GET', '/courses').as(
            'getCourseExecutions'
        );
        cy.intercept('GET', '/').as(
            'Menu'
        );

        cy.get('[data-cy="changeCourseMenuButton"]').click();
        cy.wait('@getCourseExecutions');

        cy.get('div[key="1st Semester 2023/2024"]')
          .find('div > .v-list-item__content').click();
        cy.wait('@Menu');

        cy.get('[data-cy="dashboardMenuButton"]').click();
        cy.wait('@getTeacherDashboard');

        // Test the values presented on the dashboard:
        cy.get('div[ref="numStudents"]')
            .find('animated-number')
            .should('have.attr', ':number', 5);

        cy.get('div[ref="studentsMore75CorrectQuestions"]')
            .find('animated-number')
            .should('have.attr', ':number', 1);

        cy.get('div[ref="studentsAtLeast3Quizzes"]')
            .find('animated-number')
            .should('have.attr', ':number', 0);

        cy.get('div[ref="totalQuizzes"]')
            .find('animated-number')
            .should('have.attr', ':number', 1);

        cy.get('div[ref="uniqueQuizzesSolved"]')
            .find('animated-number')
            .should('have.attr', ':number', 0.6);

        cy.get('div[ref="averageUniqueQuizzesSolved"]')
            .find('animated-number')
            .should('have.attr', ':number', 0);

        cy.get('div[ref="numAvailable"]')
            .find('animated-number')
            .should('have.attr', ':number', 3);

        cy.get('div[ref="answeredQuestionsUnique"]')
            .find('animated-number')
            .should('have.attr', ':number', 3.4);

        cy.get('div[ref="averageQuestionsAnswered"]')
            .find('animated-number')
            .should('have.attr', ':number', 4);

        cy.get('.bar-chart:nth-of-type(1) .chartjs-render-monitor')
            .should('be.visible')
            .should('have.descendants', 'rect')
            .should('have.length', 9);

        cy.get('.bar-chart:nth-of-type(2) .chartjs-render-monitor')
            .should('be.visible')
            .should('have.descendants', 'rect')
            .should('have.length', 9);

        cy.get('.bar-chart:nth-of-type(3) .chartjs-render-monitor')
            .should('be.visible')
            .should('have.descendants', 'rect')
            .should('have.length', 9);
        
        cy.contains('Logout').click();

        Cypress.on('uncaught:exception', (err, runnable) => {
        // returning false here prevents Cypress from
        // failing the test
        return false;
        });
    });

    /*
     * Second test:
     * Test if the values presented on the dashboard from 2022's course execution are correct
     * and verify that 2 bar charts are shown - 2019 and 2022 course executions.
     */
    it('teacher accesses dashboard of 2022 course execution', () => {
        cy.intercept('GET', '**/teachers/dashboards/executions/*').as(
            'getTeacherDashboard'
        );
        cy.intercept('GET', '/courses').as(
            'getCourseExecutions'
        );
        cy.intercept('GET', '/').as(
            'Menu'
        );

        cy.get('[data-cy="changeCourseMenuButton"]').click();
        cy.wait('@getCourseExecutions');

        cy.get('div[key="1st Semester 2022/2023"]')
          .find('div > .v-list-item__content').click();
        cy.wait('@Menu');

        cy.get('[data-cy="dashboardMenuButton"]').click();
        cy.wait('@getTeacherDashboard');

        // Test the values presented on the dashboard:
        cy.get('div[ref="numStudents"]')
            .find('animated-number')
            .should('have.attr', ':number', 3);

        cy.get('div[ref="studentsMore75CorrectQuestions"]')
            .find('animated-number')
            .should('have.attr', ':number', 2);

        cy.get('div[ref="studentsAtLeast3Quizzes"]')
            .find('animated-number')
            .should('have.attr', ':number', 2);

        cy.get('div[ref="totalQuizzes"]')
            .find('animated-number')
            .should('have.attr', ':number', 3);

        cy.get('div[ref="uniqueQuizzesSolved"]')
            .find('animated-number')
            .should('have.attr', ':number', 2);

        cy.get('div[ref="averageUniqueQuizzesSolved"]')
            .find('animated-number')
            .should('have.attr', ':number', 3);

        cy.get('div[ref="numAvailable"]')
            .find('animated-number')
            .should('have.attr', ':number', 0);

        cy.get('div[ref="answeredQuestionsUnique"]')
            .find('animated-number')
            .should('have.attr', ':number', 6);

        cy.get('div[ref="averageQuestionsAnswered"]')
            .find('animated-number')
            .should('have.attr', ':number', 4.333);

        cy.get('.bar-chart:nth-of-type(1) .chartjs-render-monitor')
            .should('be.visible')
            .should('have.descendants', 'rect')
            .should('have.length', 6);

        cy.get('.bar-chart:nth-of-type(2) .chartjs-render-monitor')
            .should('be.visible')
            .should('have.descendants', 'rect')
            .should('have.length', 6);

        cy.get('.bar-chart:nth-of-type(3) .chartjs-render-monitor')
            .should('be.visible')
            .should('have.descendants', 'rect')
            .should('have.length', 6);
        
        cy.contains('Logout').click();

        Cypress.on('uncaught:exception', (err, runnable) => {
        // returning false here prevents Cypress from
        // failing the test
        return false;
        });
    });

    /*
     * Third test:
     * Test if the values presented on the dashboard from 2022's course execution are correct
     * and verify that only 1 bar chart is shown - from its own course execution.
     */
    it('teacher accesses dashboard of 2019 course execution', () => {
        cy.intercept('GET', '**/teachers/dashboards/executions/*').as(
            'getTeacherDashboard'
        );
        cy.intercept('GET', '/courses').as(
            'getCourseExecutions'
        );
        cy.intercept('GET', '/').as(
            'Menu'
        );

        cy.get('[data-cy="changeCourseMenuButton"]').click();
        cy.wait('@getCourseExecutions');

        cy.get('div[key="1st Semester 2022/2023"]')
          .find('div > .v-list-item__content').click();
        cy.wait('@Menu');

        cy.get('[data-cy="dashboardMenuButton"]').click();
        cy.wait('@getTeacherDashboard');

        // Test the values presented on the dashboard:
        cy.get('div[ref="numStudents"]')
            .find('animated-number')
            .should('have.attr', ':number', 5);

        cy.get('div[ref="studentsMore75CorrectQuestions"]')
            .find('animated-number')
            .should('have.attr', ':number', 1);

        cy.get('div[ref="studentsAtLeast3Quizzes"]')
            .find('animated-number')
            .should('have.attr', ':number', 0);

        cy.get('div[ref="totalQuizzes"]')
            .find('animated-number')
            .should('have.attr', ':number', 1);

        cy.get('div[ref="uniqueQuizzesSolved"]')
            .find('animated-number')
            .should('have.attr', ':number', 1);

        cy.get('div[ref="averageUniqueQuizzesSolved"]')
            .find('animated-number')
            .should('have.attr', ':number', 1);

        cy.get('div[ref="numAvailable"]')
            .find('animated-number')
            .should('have.attr', ':number', 0);

        cy.get('div[ref="answeredQuestionsUnique"]')
            .find('animated-number')
            .should('have.attr', ':number', 4);

        cy.get('div[ref="averageQuestionsAnswered"]')
            .find('animated-number')
            .should('have.attr', ':number', 0);

        //Checks that there are no graphs
        cy.get('Bar').should('not.exist')
        
        cy.contains('Logout').click();

        Cypress.on('uncaught:exception', (err, runnable) => {
        // returning false here prevents Cypress from
        // failing the test
        return false;
        });
    });

});


