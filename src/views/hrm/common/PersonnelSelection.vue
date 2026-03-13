<!-- 按照组织架构选择人员 -->
<template>
    <div class="personnel-selection-container">
        <drawerTemplate v-model="drawer" title="选择人员" size="65%">
            <div class="apply-form-container">
              <div class="apply-form-container tw-w-full tw-overflow-hidden tw-h-full tw-box-border">
                    <userSelector @selected="handleSelected" @doubleClick="confirm"/>
                </div>
            </div>

            <template #bottom-btn>
                <el-button @click="drawer = false">取消</el-button>
                <el-button type="primary" @click="confirm" :disabled="!confirmUser">确定</el-button>
            </template>
        </drawerTemplate>
    </div>
</template>

<script setup lang="ts" name="">
import { ref, watch } from 'vue'
import userSelector from './UserSelector.vue';
import drawerTemplate from './Drawer.vue';

const drawer = ref(false)
const confirmUser = ref(false)
const selectedUser = ref('')
const emit = defineEmits(['confirm'])
// 确认
function confirm() {
    emit('confirm', selectedUser.value)
    confirmUser.value = false
    selectedUser.value = ''
    drawer.value = false
}
// 处理节点点击事件
const handleNodeClick = (data:any) => {
    console.log(data)
    if (data.type === 'user') {
        confirmUser.value = true
        selectedUser.value = data.label
    } else {
        confirmUser.value = false
        selectedUser.value = ''
    }
}

// 处理选择事件
const handleSelected = (user: any) => {
    console.log('-----------------', user)
    selectedUser.value = user
    confirmUser.value = true
}

const handleClose = () => {
    confirmUser.value = false
    selectedUser.value = ''
}

// 显示抽屉
function show() {
    drawer.value = true
}

watch(
   ()=> drawer.value,
   (newVal, oldVal)=>{
    if (!newVal) {
        confirmUser.value = false
        selectedUser.value = ''
    }
   },
)

defineExpose({
    drawer,
    show,
})
</script>

<style scoped lang="scss">
:deep(.el-drawer) {
    .el-drawer__header {
        margin: 0;

        .el-drawer__title {
            color: #303133;
            font-family: "PingFang SC";
            font-weight: 600;
            font-size: 18px;
            line-height: 26px;
            border-bottom: 1px solid #E4E7ED;
            padding-bottom: 15px;
        }

        .el-drawer__close-btn {
            border-bottom: 1px solid #E4E7ED;
            padding-bottom: 20px;
        }
    }

    .el-drawer__body {
        padding: 0;
    }
}



.apply-form-container {
    height: 100%;
    // background-color: red;
    position: relative;


    .address-book-container{
        // height: 95%;
        overflow: auto;
    }

    .apply-submit {
        background-color: #fff;
        position: absolute;
        bottom: 0;
        right: 0;
        width: 100%;
        margin-top: 20px;
        text-align: right;
        padding: 0px 24px;
        box-shadow: 0px 0px 12px #0000001f;
    }
}

</style>
