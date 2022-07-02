package com.example.demo.po.task;

import com.example.demo.vo.task.RuleVO;

public class Rule {
    private Integer id;

    private Boolean professionalAbilityRanking;

    private Boolean sortByTaskCategory;

    private Boolean testEquipmentSorting;

    private Boolean selected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getProfessionalAbilityRanking() {
        return professionalAbilityRanking;
    }

    public void setProfessionalAbilityRanking(Boolean professionalAbilityRanking) {
        this.professionalAbilityRanking = professionalAbilityRanking;
    }

    public Boolean getSortByTaskCategory() {
        return sortByTaskCategory;
    }

    public void setSortByTaskCategory(Boolean sortByTaskCategory) {
        this.sortByTaskCategory = sortByTaskCategory;
    }

    public Boolean getTestEquipmentSorting() {
        return testEquipmentSorting;
    }

    public void setTestEquipmentSorting(Boolean testEquipmentSorting) {
        this.testEquipmentSorting = testEquipmentSorting;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Rule(RuleVO rule){
        this.id=rule.getId();
        this.professionalAbilityRanking=rule.isProfessionalAbilityRanking();
        this.sortByTaskCategory=rule.isSortByTaskCategory();
        this.testEquipmentSorting=rule.isTestEquipmentSorting();
        this.selected=rule.getSelected();
    }

    public Rule(){}
}