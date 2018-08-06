<template>
    <div>
        <div style="width:100%;">
            <div><h1>运行报告</h1></div>
            <el-row :gutter="20">
                <el-col :span="8">
                    <div class="first_cl info-box">
                        <span class="info-box-icon"><i class="el-icon-goods"></i></span>
                        <div class="info-box-content">
                            <span style="height:20px">任务数量</span>
                            <span style="font-weight: bold;font-size: 18px; height:25px;">8</span>
                            <div class="progress"></div>
                            <span style="height:20px;">调度中心运行的任务数量</span>
                        </div>
                    </div> 
                </el-col>
                <el-col :span="8">
                    <div class="second_cl info-box">
                        <span class="info-box-icon"><i class="el-icon-date"></i></span>
                        <div class="info-box-content">
                            <span style="height:20px">调度次数</span>
                            <span style="font-weight: bold;font-size: 18px;height:25px;">12</span>
                            <div class="progress"></div>
                            <span style="height:20px">调度中心触发的调度次数</span>
                        </div>
                    </div>
                </el-col>
                <el-col :span="8">
                    <div class="third_cl info-box">
                        <span class="info-box-icon"><i class="el-icon-share"></i></span>
                        <div class="info-box-content">
                            <span style="height:20px">执行器数量</span>
                            <span style="font-weight: bold;font-size: 18px;height:25px;">30</span>
                            <div class="progress"></div>
                            <span style="height:20px">调度中心在线的执行器机器数量</span>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-row>
                <div class="box">
                    <div class="box-header"><h3 class="box-title">调度报表</h3></div>
                </div>
            </el-row>
        </div>
    </div>
</template>
<style scoped>
    
    .info-box-icon{
        border-top-left-radius: 2px;
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
        border-bottom-left-radius: 2px;
        display: block;
        float: left;
        height: 90px;
        width: 90px;
        text-align: center;
        font-size: 45px;
        line-height: 90px;
        background: rgba(0,0,0,0.2);
        color:#fff !important;
    }
    .info-box{
        display: block;
        min-height: 90px;
        background: #fff;
        width: 100%;
        box-shadow: 0 1px 1px rgba(0,0,0,0.1);
        border-radius: 2px;
        margin-bottom: 15px;
    }
    .first_cl{
        background-color: #00c0ef !important;
    }
    .second_cl{
        background-color: #f39c12 !important;
    }
    .third_cl{
        background-color: #00a65a !important;
    }
    .progress{
        height: 2px;
        margin: 5px -10px 5px -10px;
        overflow: hidden;
        background-color: #f5f5f5;
        border-radius: 4px;
        -webkit-box-shadow: inset 0 1px 2px rgba(0,0,0,.1);
        box-shadow: inset 0 1px 2px rgba(0,0,0,.1)
    }
    .info-box-content{
        padding: 5px 10px;
        margin-left: 90px;
    }
    .info-box-content span{
        display: block;
        font-size: 14px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        /* padding-left: 15px; */
        color:#f5f5f5;
        /* height: 25px; */
    }
    .box{
        position: relative;
        border-radius: 3px;
        background: #ffffff;
        border-top: 3px solid #d2d6de;
        margin-bottom: 20px;
        width: 100%;
        box-shadow: 0 1px 1px rgba(0,0,0,0.1);
    }
</style>


<script>
export default {
    
}
</script>
