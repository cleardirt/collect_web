package com.example.demo.vo.task;

import com.example.demo.po.task.Rule;
import lombok.Data;

@Data
public class RuleVO {
    private Integer id;

    private boolean professionalAbilityRanking;

    private boolean sortByTaskCategory;

    private boolean testEquipmentSorting;

    private Boolean selected;

    public RuleVO(boolean professionalAbilityRanking,boolean sortByTaskCategory,boolean testEquipmentSorting){
        this.professionalAbilityRanking= professionalAbilityRanking;
        this.sortByTaskCategory=sortByTaskCategory;
        this.testEquipmentSorting=testEquipmentSorting;
    }

    public RuleVO(Rule rule){
        this.id=rule.getId();
        this.professionalAbilityRanking=rule.getProfessionalAbilityRanking();
        this.sortByTaskCategory=rule.getSortByTaskCategory();
        this.testEquipmentSorting=rule.getTestEquipmentSorting();
        this.selected=rule.getSelected();
    }

    public RuleVO(){}
}
