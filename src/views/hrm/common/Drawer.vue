<template>
    <div class="">
        <el-drawer v-model="drawer" :title="props.title" destroy-on-close :size="props.size"
            @close="emit('update:modelValue', false)">
            <template #header>
                <div class="el-drawer__title">
                    <span>{{ title }}</span>
                    <span class="title-notice"><slot name="title-notice"></slot></span>
                </div>
            </template>
            <div class="drawer-content-container">
                <div class="content" :style="!props.bottomBtn ? 'height: auto' : ''">
                    <slot name="content"></slot>
                    <slot></slot>
                </div>

                <!-- 底部按钮插槽：默认显示原按钮，可通过插槽自定义 -->
                <div class="bottom-btn" v-if="props.bottomBtn">
                    <slot name="bottom-btn">
                        <!-- 后备内容：默认按钮 -->
                        <el-button @click="reset">重置</el-button>
                        <el-button type="primary" @click="confirm">确定</el-button>
                    </slot>
                </div>
            </div>
        </el-drawer>
    </div>
</template>

<script setup lang="ts" name="">
import { ref, watch } from 'vue';
interface Props {
    modelValue?: boolean;
    size?: string | number; //number 类型时, 以像素为单位, 字符串类型时, 以百分比为单位
    bottomBtn?: boolean;
    title?: string;
}
const props = withDefaults(defineProps<Props>(), {
    size: '50%',
    bottomBtn: true,
    title: '详情',
})
const emit = defineEmits(['update:modelValue', 'confirm', 'reset'])
const drawer = ref(props.modelValue || false);

const reset = () => {
    emit('reset')
}

const confirm = () => {
    emit('confirm')
}

const showDrawer = () => {
    drawer.value = true;
}

watch(
    () => props.modelValue,
    (newVal) => {
        drawer.value = newVal;
    },
)

defineExpose({
    drawer,
    showDrawer,
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

        .title-notice {
            margin-left: 10px;
            color: #e6a23c;
            font-family: "PingFang SC";
            font-weight: 400;
            font-size: 14px;
            line-height: 22px;
            letter-spacing: 0px;
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

.drawer-content-container {
    height: 100%;
    position: relative;
    // margin-bottom: 66px;

    .content {
        height: calc(100% - 48px);
        overflow-y: auto;
    }

    .bottom-btn {
        width: 100%;
        background-color: #fff;
        position: absolute;
        bottom: 0;
        right: 0;
        margin-top: 20px;
        text-align: right;
        padding: 8px 24px;
        box-shadow: 0px 0px 12px #0000001f;
        z-index: 999;
    }
}
</style>

<!-- 用法1：
<drawer ref="drawerRef">
    <template #content>
        <div class="content">
            详情
        </div>
    </template>
</drawer>
drawerRef.value.showDrawer()

用法2：
<drawer v-model="show">
    <template #content>
        <div class="content">
            详情
        </div>
    </template>
</drawer>
const show = ref(false)
-->
