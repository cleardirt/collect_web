package com.example.demo.vo.report;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ReportTreeNodeVO {
    ArrayList<ReportTreeNodeVO> child;
    Integer reportId;
    Integer fatherId;
    Integer taskId;
    public ReportTreeNodeVO(Integer reportId, Integer fatherId, Integer taskId){
        this.child=new ArrayList<>();
        this.reportId=reportId;
        this.fatherId=fatherId;
        this.taskId=taskId;
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj)   //判断对象地址是否相等，如果是就不用判断，提高效率
            return true;
        if (obj==null)
            return false;
        if (!(obj instanceof ReportTreeNodeVO)) {
            return false;
        }
        ReportTreeNodeVO reportTreeNode=(ReportTreeNodeVO) obj;
        return reportTreeNode.reportId.equals(this.reportId)&&reportTreeNode.fatherId.equals(this.fatherId)&&reportTreeNode.taskId.equals(this.taskId);
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((reportId == null) ? 0 : reportId.hashCode());
        result = prime * result + ((fatherId == null) ? 0 : fatherId.hashCode());
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
        return result;
    }
}