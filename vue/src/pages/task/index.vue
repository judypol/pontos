<template>
    <div class="tools">
        <el-form :inline="true" :model="searchModel">
            <el-form-item label="执行器">
                <el-select v-model="searchModel.jobGroup">
                    <el-option
                        v-for="item in executors"
                        :key="item.id"
                        :label="item.title"
                        :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="任务描述">
                <el-input v-model="searchModel.jobDesc" ></el-input>
            </el-form-item>
            <el-form-item label="JobHandler">
                <el-autocomplete v-model="searchModel.executorHandler" :fetch-suggestions="(queryString,cb)=>{querySearch(queryString,cb,'jobHandler')}" @select="handleSelect"></el-autocomplete>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click.prevent="onSearch">搜索</el-button>
                <el-button type="success" @click.prevent="openAddDialog('新增任务')">新增任务</el-button>
            </el-form-item>
        </el-form>
        <div class="tb">
            <el-table border :data="tbModel.data">
                <el-table-column label="任务ID" prop="id" width="80"></el-table-column>
                <el-table-column label="任务描述" prop="jobDesc"></el-table-column>
                <el-table-column label="运行模式" >
                    <template slot-scope="scope">
                        {{scope.row.glueType}} - {{scope.row.executorHandler}}
                    </template>
                </el-table-column>
                <el-table-column label="Cron" prop="jobCron"></el-table-column>
                <el-table-column label="负责人" prop="author"></el-table-column>
                <el-table-column label="状态" >
                    <template slot-scope="scope">
                        <template v-if="scope.row.jobStatus=='NORMAL'">
                            <el-tag type="success">{{scope.row.jobStatus}}</el-tag>
                        </template>
                        <template v-else>
                            <el-tag type="warning">{{scope.row.jobStatus}}</el-tag>
                        </template>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="250">
                    <template slot-scope="scope">
                        <el-button @click="triggerJob(scope.row)" type="text" size="small">执行</el-button>
                        <template v-if="scope.row.jobStatus=='NORMAL'">
                            <el-button type="text" size="small" @click.prevent="pause(scope.row)">暂停</el-button>
                        </template>
                        <template v-else>
                            <el-button type="text" size="small" @click.prevent="resume(scope.row)">恢复</el-button>
                        </template>
                        <el-button type="text" size="small" @click.prevent="handleLog(scope.row)">日志</el-button>
                        <el-button type="text" size="small" @click.prevent="editTaskInfo(scope.row)">编辑</el-button>
                        <!-- <el-button type="text" size="small" @click.prevent="removeTask(scope.row)">删除</el-button> -->
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination background layout="prev, pager, next" :total="tbModel.total" :current-page.sync="currentPage" :page-size="15" @current-change="handleCurrentChange"></el-pagination>
        </div>
        <task-info :title="dialog.title" :model="dialogModel" @refresh="onSearch"></task-info>
    </div>
</template>
<style>
    .el-dialog__body {
        padding:20px 20px 0 20px;
    }
    .el-form-item{
        margin-bottom: 15px;
    }
</style>
<script>
import taksInfo from '../../components/taskInfo';

export default {
    data(){
        return {
            searchModel:{
                start:0,
                length:15,
                jobGroup:'',
                jobDesc:'',
                executorHandler:'',
            },
            currentPage:1,
            executors:[],
            jobDescriptions:[],
            jobHandlers:[],
            emptyText:' ',
            tbModel:{
                total:150,
                data:[]
            },
            dialog:{
                title:'XXX',
            },
            dialogModel:{
                id:0,
                jobGroup:null,
                jobCron:'',
                jobDesc:'',
                author:'',
                alarmEmail:'',
                executorRouteStrategy:'FIRST',
                executorHandler:'',
                executorParam:'',
                executorBlockStrategy:'SERIAL_EXECUTION',
                executorFailStrategy:'NULL',
                executorTimeout:60,
                glueType:'BEAN',
                childJobId:'',
                holiday:'',
                visible:false,
            },
            rules: {
                name: [
                    { required: true, message: '请输入活动名称', trigger: 'blur' },
                    { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
                ],
                region: [
                    { required: true, message: '请选择活动区域', trigger: 'change' }
                ],
                date1: [
                    { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
                ],
                date2: [
                    { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
                ],
                type: [
                    { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
                ],
                resource: [
                    { required: true, message: '请选择活动资源', trigger: 'change' }
                ],
                desc: [
                    { required: true, message: '请填写活动形式', trigger: 'blur' }
                ]
            }
        }
    },
    methods:{
        onSearch(){
            var that=this;
            that.searchModel.start=(that.currentPage-1)*15;
            this.$axios.post('/jobinfo/pageList',that.searchModel).then((res)=>{
                that.tbModel.data=res.data;
                that.tbModel.total=res.recordsTotal;
            });
        },
        onAddTask(){
            this.$msg('addTask');
        },
        handleSelect(){

        },
        querySearch(queryString,cb,excutors){
            var result=queryString?this.$store.state.executors.filter(this.createFilter(queryString)):this.$store.state.executors;
            cb(result);
        },
        createFilter(queryString) {
            return (restaurant) => {
                return (restaurant.title.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
            };
        },
        handleCurrentChange(val){
            this.onSearch();
        },
        openAddDialog(title){
            this.dialog.title=title;
            this.dialogModel={
                id:0,
                jobGroup:this.$store.state.executors[0].id,
                jobCron:'',
                jobDesc:'',
                author:'',
                alarmEmail:'',
                executorRouteStrategy:'FIRST',
                executorHandler:'',
                executorParam:'',
                executorBlockStrategy:'SERIAL_EXECUTION',
                executorFailStrategy:'NULL',
                executorTimeout:60,
                glueType:'BEAN',
                childJobId:'',
                holiday:'',
                visible:true,
            };
        },
        editTaskInfo(row){
            this.dialog.title="编辑任务";
            for(var p in this.dialogModel){
                this.dialogModel[p]=row[p];
            }
            this.dialogModel.visible=true;
        },
        triggerJob(row){
            this.$confirm('确定执行？','系统消息').then(()=>{
                this.$axios.post('/jobinfo/trigger',{id:row.id})
                .then((res)=>{
                    if(res.code===200){
                        this.$alert('执行成功');
                    }else{
                        this.$message.error(res.msg);
                    }
                })
            }).catch(()=>{
                    console.log('quxiao');
            });
        },
        pause(row){
            this.$confirm('确定暂停？','系统消息').then(()=>{
                this.$axios.post('/jobinfo/pause',{id:row.id})
                .then((res)=>{
                    if(res.code===200){
                        this.$alert('暂停成功').then(()=>{
                            this.onSearch();
                        });
                    }else{
                        this.$message.error(res.msg);
                    }
                })
            }).catch(()=>{
                    console.log('pause');
            });
        },
        resume(row){
            this.$confirm('确定恢复？','系统消息').then(()=>{
                this.$axios.post('/jobinfo/resume',{id:row.id})
                .then((res)=>{
                    if(res.code===200){
                        this.$alert('恢复成功').then(()=>{
                            this.onSearch();
                        });
                    }else{
                        this.$message.error(res.msg);
                    }
                })
            }).catch(()=>{
                    console.log('resume');
            });
        },
        removeTask(row){
            this.$confirm('确定删除？','系统消息').then(()=>{
                this.$axios.post('/jobinfo/remove',{id:row.id})
                .then((res)=>{
                    if(res.code===200){
                        this.$alert('删除成功').then(()=>{
                            this.onSearch();
                        });
                    }else{
                        this.$message.error(res.msg);
                    }
                })
            }).catch(()=>{
                    console.log('删除');
            });
        },
        handleLog(row){
            this.$router.push({path:'/log',query:{jobId:row.id,jobGroup:row.jobGroup}});
        },
    },
    created(){
        // this.$store.dispatch('setExcutors');
        // this.excutors=this.$store.state.excutors;
        // this.searchModel.jobGroup=this.excutors[0].id;
        var that=this;
        this.$axios.post('/jobinfo/groupList',{filter:''})
        .then((res)=>{
            if(res.code==200){
                this.$store.commit('setExecutors',res.content);
                that.executors=res.content;
                that.searchModel.jobGroup=that.executors[0].id;
                that.onSearch();
            }
            
        });
    },
    components:{
        'task-info':taksInfo
    }
}
</script>
