package com.mycompany.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;

import com.mycompany.pages.StudentReportPage;

public class EndUserSteps extends ScenarioSteps {

    public EndUserSteps(Pages pages) {
        super(pages);
    }

    @Step
    public void list_students() {
        onStudentReportPage().list_students();
    }
    
    private StudentReportPage onStudentReportPage() {
        return getPages().currentPageAt(StudentReportPage.class);
    }

}
