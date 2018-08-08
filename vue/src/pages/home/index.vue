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
                            <span style="font-weight: bold;font-size: 18px; height:25px;">{{dashboard.jobInfoCount}}</span>
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
                            <span style="font-weight: bold;font-size: 18px;height:25px;">{{dashboard.jobLogCount}}</span>
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
                            <span style="font-weight: bold;font-size: 18px;height:25px;">{{dashboard.executorCount}}</span>
                            <div class="progress"></div>
                            <span style="height:20px">调度中心在线的执行器机器数量</span>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-row class="box">
                <el-row>
                    <el-col :span="4">
                        <div class="box-header"><h3 class="box-title" style="width:500px">调度报表</h3></div>
                    </el-col>
                    <el-col :span="20">
                        <el-date-picker v-model="searchDates" :default-time="['00:00:00','23:00:00']"
                        value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"> </el-date-picker>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="16">
                        <chart class="lineChart" :options="lineOptions"></chart>
                    </el-col>
                    <el-col :span="8">
                        <chart class="polarChart" :options="polarOptions"></chart>
                    </el-col>
                </el-row>
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
    .lineChart {
        height: 350px;
        width:100%;
    }
    .polarChart{
        height: 350px;
        width:100%;
    }
</style>


<script>
import moment from 'moment'
import Vue from 'vue'
import ECharts from 'vue-echarts/components/ECharts'
import 'echarts/lib/chart/line'
import 'echarts/lib/chart/pie'
import 'echarts/lib/component/legend'
import 'echarts/lib/component/title'
import 'echarts/lib/component/tooltip'

export default {
    components:{
        chart:ECharts
    },
    data(){
        return{
            lineOptions: {  },
            polarOptions: {  },
            searchDates:[moment().subtract(30,'days').format('YYYY-MM-DD 00:00:00'),moment().format('YYYY-MM-DD 23:59:00')],
            dashboard:{
                jobInfoCount:0,
                jobLogCount:0,
                jobLogSuccessCount:0,
                executorCount:0,
            }
        }
    },
    methods:{
        onSearch(){
            var postData={
                startDate:this.searchDates[0],
                endDate:this.searchDates[1],
            }
            this.$axios.post('/chartInfo',postData).then((res)=>{
                if(res.code===200){
                    this.refreshLine(res.content);
                    this.refreshPolay(res.content);
                }else{
                    this.$message.error(res.msg);
                }
            })
        },
        refreshLine(data){
            this.lineOptions={
               title: {
                   text: '日期分布图'
               },
               tooltip : {
                   trigger: 'axis',
                   axisPointer: {
                       type: 'cross',
                       label: {
                           backgroundColor: '#6a7985'
                       }
                   }
               },
               legend: {
                   data:['成功', '失败', '运行中']
               },
               toolbox: {
                   feature: {
                       /*saveAsImage: {}*/
                   }
               },
               grid: {
                   left: '3%',
                   right: '4%',
                   bottom: '3%',
                   containLabel: true
               },
               xAxis : [
                   {
                       type : 'category',
                       boundaryGap : false,
                       data : data.triggerDayList
                   }
               ],
               yAxis : [
                   {
                       type : 'value'
                   }
               ],
               series : [
                   {
                       name:'成功',
                       type:'line',
                       stack: 'Total',
                       areaStyle: {normal: {}},
                       data: data.triggerDayCountSucList
                   },
                   {
                       name:'失败',
                       type:'line',
                       stack: 'Total',
                       label: {
                           normal: {
                               show: true,
                               position: 'top'
                           }
                       },
                       areaStyle: {normal: {}},
                       data: data.triggerDayCountFailList
                   },
                   {
                       name:'运行中',
                       type:'line',
                       stack: 'Total',
                       areaStyle: {normal: {}},
                       data: data.triggerDayCountRunningList
                   }
               ],
                color:['#00A65A', '#c23632', '#F39C12']
            }
        },
        refreshPolay(data){
            this.polarOptions={
                title : {
                text: '成功比例图' ,
                /*subtext: 'subtext',*/
                x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{b} : {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    data: ['成功', '失败', '运行中' ]
                },
                series : [
                    {
                        //name: '分布比例',
                        type: 'pie',
                        radius : '55%',
                        center: ['50%', '60%'],
                        data:[
                            {
                                name:'成功',
                                value:data.triggerCountSucTotal
                            },
                            {
                                name:'失败',
                                value:data.triggerCountFailTotal
                            },
                            {
                                name:'运行中',
                                value:data.triggerCountRunningTotal
                            }
                        ],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ],
                color:['#00A65A', '#c23632', '#F39C12']
            }
        },
        dashboardInfo(){
            this.$axios.post('/dashboard',{}).then((res)=>{
                if(res.code===200){
                    this.dashboard=res.content;
                }else{
                    this.$message.error(res.msg);
                }
            })
        }
    },
    created(){
        this.onSearch();
        this.dashboardInfo();
    }
}
</script>
