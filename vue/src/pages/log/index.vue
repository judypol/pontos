<template>
    <div>
        <div class="search">
            <el-form :inline="true" :model="searchModel">
                <el-form-item label="执行器">
                    <el-select v-model="searchModel.jobGroup" @change="getJobsByGroup">
                        <el-option v-for="item in executors" :key="item.id" :label="item.title" :value="item.id"> </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="任务">
                    <el-select v-model="searchModel.jobId">
                        <el-option v-for="item in jobs" :key="item.id" :label="item.jobDesc" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="searchModel.logStatus">
                        <el-option v-for="item in logStatus" :key="item.value" :label="item.name" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="调度时间">
                    <el-date-picker v-model="searchModel.dateRange" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" range-separator="至" start-placeholder="开始日期" 
                    end-placeholder="结束日期" unlink-panels> </el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click.prevent="onSearch">搜 索</el-button>
                    <el-button type="warning" @click.prevent="()=>{dialogVisible=true;}">清 理</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="tb">
            <el-table :border="true" :data="tbModel.data">
                <el-table-column label="任务Id" prop="jobId"></el-table-column>
                <el-table-column label="调度时间" prop="triggerTime"></el-table-column>
                <el-table-column label="调度结果" prop="triggerCode">
                    <template slot-scope="scope">
                        <template v-if="scope.row.triggerCode===500">
                            <span style="color:red">失败</span>
                        </template>
                        <template v-else>
                            <span style="color:green">成功</span>
                        </template>
                    </template>
                </el-table-column>
                <el-table-column label="调度备注" prop="triggerMsg">
                    <template slot-scope="scope">
                        <el-popover trigger="click" placement="top">
                            <div v-html="scope.row.triggerMsg"></div>
                            <div slot="reference" class="name-wrapper">
                                <el-tag style="cursor:pointer;" size="medium">查看</el-tag>
                            </div>
                        </el-popover>
                    </template>
                </el-table-column>
                <el-table-column label="执行时间" prop="handleTime"></el-table-column>
                <el-table-column label="执行结果" prop="handleCode">
                    <template slot-scope="scope">
                        <template v-if="scope.row.handleCode===0">
                            <span style="color:red"></span>
                        </template>
                        <template v-else-if="scope.row.handleCode===200">
                            <span style="color:green">成功</span>
                        </template>
                        <template v-else>
                            <span style="color:red">失败</span>
                        </template>
                    </template>
                </el-table-column>
                <el-table-column label="执行备注" prop="handleMsg">
                    <template slot-scope="scope">
                        <template v-if="scope.row.handleMsg===null">
                            <span>无</span>
                        </template>
                        <template v-else>
                            <el-popover trigger="click" placement="top">
                                <div v-html="scope.row.handleMsg"></div>
                                <div slot="reference" class="name-wrapper">
                                    <el-tag style="cursor:pointer;" size="medium">查看</el-tag>
                                </div>
                            </el-popover>
                        </template>
                        
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button size="small" type="primary" @click.prevent="logDetailCat(scope.row)">查看日志</el-button>
                        <el-button size="small" type="warning">终止任务</el-button>
                    </template>
                    
                </el-table-column>
            </el-table>
            <el-pagination :background="true" layout="prev, pager, next" :total="tbModel.recordsTotal" :current-page.sync="currentPage" @current-change="onSearch"></el-pagination>
        </div>
        <el-dialog title="删除日志" :visible.sync="dialogVisible" width="300px" class="clearDialog">
            <div>
                <el-radio v-model="radio" label="1" :border="true">清理一个月之前日志数据</el-radio>
            </div>
            <div>
                <el-radio v-model="radio" label="2" :border="true">清理三个月之前日志数据</el-radio>
            </div>
            <div>
                <el-radio v-model="radio" label="3" :border="true">清理六个月之前日志数据</el-radio>
            </div>
            <div>
                <el-radio v-model="radio" label="4" :border="true">清理一年之前日志数据</el-radio>
            </div>
            <div>
                <el-radio v-model="radio" label="5" :border="true">清理一千条以前日志数据</el-radio>
            </div>
            <div>
                <el-radio v-model="radio" label="6" :border="true">清理一万条以前日志数据</el-radio>
            </div>
            <div>
                <el-radio v-model="radio" label="7" :border="true">清理三万条以前日志数据</el-radio>
            </div>
            <div>
                <el-radio v-model="radio" label="8" :border="true">清理十万条以前日志数据</el-radio>
            </div>
            <div>
                <el-radio v-model="radio" label="9" :border="true">所有</el-radio>
            </div>
            
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="deleteLogs">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog title="日志详情" :visible.sync="logDetailPage.dialogVisible">
            <div v-html="logDetailPage.logContent" class="logDetail"></div>
            <span slot="footer" class="dialog-footer">
                <!-- <el-button @click="dialogVisible = false">取 消</el-button> -->
                <el-button type="primary" @click="logDetailPage.dialogVisible = false">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<style scoped>
    /* .el-radio{
        padding:5px;
    } */
    .clearDailog >div{
        padding-top: 5px;
    }
    .logDetail{
        font-size: 16px;
        font-weight: 500;
        line-height: 30px;
        color:green;
    }
</style>

<script>
import moment from 'moment'
export default {
    data(){
        return{
            searchModel:{
                start:0,
                length:15,
                jobGroup:-1,
                jobId:-1,
                logStatus:-1,
                filterTime:'',
                dateRange:[moment(new Date()).format('YYYY-MM-DD 00:00:00'),moment(new Date()).format('YYYY-MM-DD 23:59:59')]
            },
            tbModel:{
                data:[],
                recordsTotal:0
            },
            jobs:[],
            logStatus:this.$store.state.logStatus,
            currentPage:1,
            dialogVisible:false,
            radio:"1",
            logDetailPage:{
                logContent:'',
                dialogVisible:false
            }
        }
    },
    methods:{
        getFilterTime(){
            if(this.searchModel.dateRange&&this.searchModel.dateRange.length==2){
                this.searchModel.filterTime=this.searchModel.dateRange[0]+'-'+this.searchModel.dateRange[1];
            }
        },
        onSearch(){
            this.getFilterTime();
            this.searchModel.start=this.currentPage-1;
            this.$axios.post('/joblog/pageList',this.searchModel).then((res)=>{
                if(res.data){
                    this.tbModel.data=JSON.parse(res.data);
                    this.tbModel.recordsTotal=res.recordsTotal;
                }
            });
        },
        getJobsByGroup(){
            this.$axios.post('/joblog/getJobsByGroup',{jobGroup:this.searchModel.jobGroup})
                .then((res)=>{
                    if(res.code===200){
                        this.jobs=res.content;
                        this.jobs.splice(0,0,{id:-1,jobDesc:'全部'});
                        this.searchModel.jobId=-1;
                    }
                })
        },
        deleteLogs(){
            this.$confirm('是否删除？','系统提示')
            .then(()=>{

                this.$axios.post('/joblog/clearLog',{jobGroup:this.searchModel.jobGroup,jobId:this.searchModel.jobId,type:this.radio})
                    .then((res)=>{
                        if(res.code===200){
                            this.$message('删除成功！');
                            this.onSearch();
                        } 
                    });
                this.dialogVisible = false;
            }).catch(()=>{
                this.dialogVisible = false;
            });
            
        },
        logDetailCat(row){
            this.logDetailPage.logContent='';
            this.$axios.post('/joblog/logDetailCat',row).then((res)=>{
                if(res.code===200){
                    this.logDetailPage.logContent=res.content.logContent;
                    this.logDetailPage.dialogVisible=true;
                }else{
                    this.$message.error(res.msg);
                }
            });
        }
    },
    computed:{
        executors:function(){
            var groups=this.$store.state.executors;
            groups.splice(0,0,{id:-1,title:'全部'});
            return groups;
        }
    },
    created(){
        this.$store.dispatch('setExecutors');
        this.searchModel.jobGroup=this.$route.query.jobGroup?this.$route.query.jobGroup:-1;
        this.getJobsByGroup();
        this.searchModel.jobId=this.$route.query.jobId?this.$route.query.jobId:-1;
        this.onSearch();
        // that.executors=that.$store.state.executors;
        //     that.searchModel.jobGroup=that.executors[0].id;
        //     that.getJobsByGroup();
    },
    
}
</script>
