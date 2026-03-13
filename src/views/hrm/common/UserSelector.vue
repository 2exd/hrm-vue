<template>
  <div class="p-2">
    <el-row :gutter="20">
      <!-- 部门树 -->
      <el-col
        :lg="4"
        :xs="24"
        style=""
      >
        <el-card shadow="hover">
          <el-tree
            ref="deptTreeRef"
            :data="deptOptions"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            :props="{ label: 'label', children: 'children' } as any"
            class="mt-2"
            default-expand-all
            highlight-current
            node-key="id"
            @node-click="handleNodeClick"
          />
        </el-card>
      </el-col>
      <el-col
        :lg="20"
        :xs="24"
      >
        <transition
          :enter-active-class="proxy?.animate.searchAnimate.enter"
          :leave-active-class="proxy?.animate.searchAnimate.leave"
        >
          <div
            v-show="showSearch"
            class="mb-[10px]"
          >
            <el-card shadow="hover">
              <el-form
                ref="queryFormRef"
                :inline="true"
                :model="queryParams"
              >
                <el-form-item
                  label="用户名称"
                  prop="userName"
                >
                  <el-input
                    v-model="queryParams.userName"
                    clearable
                    placeholder="请输入用户名称"
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>
                <el-form-item
                  label="手机号码"
                  prop="phonenumber"
                >
                  <el-input
                    v-model="queryParams.phonenumber"
                    clearable
                    placeholder="请输入手机号码"
                    @keyup.enter="handleQuery"
                  />
                </el-form-item>

                <el-form-item>
                  <el-button
                    icon="Search"
                    type="primary"
                    @click="handleQuery"
                  >搜索
                  </el-button>
                  <el-button
                    icon="Refresh"
                    @click="resetQuery"
                  >重置
                  </el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </div>
        </transition>

        <el-card shadow="hover">
          <el-table
            ref="pageTableRef"
            v-loading="loading"
            :current-row-key="currentRow?.userId"
            :data="userList"
            border
            highlight-current-row
            @current-change="handleCurrentChange"
            @row-dblclick="handleDoubleClick"
          >
            <el-table-column
              v-if="columns[1].visible"
              key="userName"
              :show-overflow-tooltip="true"
              align="center"
              label="用户名称"
              prop="userName"
            >
              <template #default="scope">
                <span>
                  {{ scope.row.userName }}
                   <el-icon
                     v-if="scope.row.userId == currentRow?.userId"
                   ><Select
                    color="primary"
                   /></el-icon>
                </span>
              </template>
            </el-table-column>

            <el-table-column
              v-if="columns[3].visible"
              key="deptName"
              :show-overflow-tooltip="true"
              align="center"
              label="部门"
              prop="deptName"
            />
            <el-table-column
              v-if="columns[4].visible"
              key="phonenumber"
              align="center"
              label="手机号码"
              prop="phonenumber"
              width="120"
            />
          </el-table>

          <pagination
            v-show="total > 0"
            v-model:limit="queryParams.pageSize"
            v-model:page="queryParams.pageNum"
            :total="total"
            @pagination="getList"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script
  lang="ts"
  name="User"
  setup
>
import api from "@/api/system/user";
import { UserForm, UserQuery, UserVO } from "@/api/system/user/types";
import { DeptTreeVO, DeptVO } from "@/api/system/dept/types";
import { RoleVO } from "@/api/system/role/types";
import { PostVO } from "@/api/system/post/types";
import { optionselect } from "@/api/system/post";
import {
  ElTable,
} from "element-plus";
import { Select } from "@element-plus/icons-vue";
// 新增：导入Check图标

const router = useRouter();
const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const {
  sys_normal_disable,
  sys_user_sex
} = toRefs<any>(proxy?.useDict("sys_normal_disable", "sys_user_sex"));
const userList = ref<UserVO[]>();
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<number | string>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const dateRange = ref<[DateModelType, DateModelType]>(["", ""]);
const deptName = ref("");
const deptOptions = ref<DeptTreeVO[]>([]);
const enabledDeptOptions = ref<DeptTreeVO[]>([]);
const initPassword = ref<string>("");
const postOptions = ref<PostVO[]>([]);
const roleOptions = ref<RoleVO[]>([]);

// 列显隐信息
const columns = ref<FieldOption[]>([
  { key: 0, label: `用户编号`, visible: false, children: [] },
  { key: 1, label: `用户名称`, visible: true, children: [] },
  { key: 2, label: `用户昵称`, visible: true, children: [] },
  { key: 3, label: `部门`, visible: true, children: [] },
  { key: 4, label: `手机号码`, visible: true, children: [] },
  { key: 5, label: `状态`, visible: true, children: [] },
  { key: 6, label: `创建时间`, visible: true, children: [] }
]);

const deptTreeRef = ref<ElTreeInstance>();
const queryFormRef = ref<ElFormInstance>();
const userFormRef = ref<ElFormInstance>();
const uploadRef = ref<ElUploadInstance>();
const formDialogRef = ref<ElDialogInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ""
});

const initFormData: UserForm = {
  userId: undefined,
  deptId: undefined,
  userName: "",
  nickName: undefined,
  password: "",
  phonenumber: undefined,
  email: undefined,
  sex: undefined,
  status: "0",
  remark: "",
  postIds: [],
  roleIds: []
};

const initData: PageData<UserForm, UserQuery> = {
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userName: "",
    phonenumber: "",
    status: "",
    deptId: "",
    roleId: ""
  },
  rules: {
    userName: [
      { required: true, message: "用户名称不能为空", trigger: "blur" },
      {
        min: 2,
        max: 20,
        message: "用户名称长度必须介于 2 和 20 之间",
        trigger: "blur"
      }
    ],
    nickName: [
      {
        required: true,
        message: "用户昵称不能为空",
        trigger: "blur"
      }
    ],
    password: [
      { required: true, message: "用户密码不能为空", trigger: "blur" },
      {
        min: 5,
        max: 20,
        message: "用户密码长度必须介于 5 和 20 之间",
        trigger: "blur"
      },
      {
        pattern: /^[^<>"'|\\]+$/,
        message: "不能包含非法字符：< > \" ' \\ |",
        trigger: "blur"
      }
    ],
    email: [
      {
        type: "email",
        message: "请输入正确的邮箱地址",
        trigger: ["blur", "change"]
      }
    ],
    phonenumber: [
      {
        pattern: /^1[3456789][0-9]\d{8}$/,
        message: "请输入正确的手机号码",
        trigger: "blur"
      }
    ],
    roleIds: [{ required: true, message: "用户角色不能为空", trigger: "blur" }]
  }
};
const data = reactive<PageData<UserForm, UserQuery>>(initData);

const {
  queryParams,
  form,
  rules
} = toRefs<PageData<UserForm, UserQuery>>(data);

/** 通过条件过滤节点  */
const filterNode = (value: string, data: any) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};
/** 根据名称筛选部门树 */
watchEffect(
  () => {
    deptTreeRef.value?.filter(deptName.value);
  },
  {
    flush: "post" // watchEffect会在DOM挂载或者更新之前就会触发，此属性控制在DOM元素更新后运行
  }
);

/** 查询用户列表 */
const getList = async () => {
  loading.value = true;
  queryParams.value.isHide = 0;
  const res = await api.listUser(proxy?.addDateRange(queryParams.value, dateRange.value));
  loading.value = false;
  userList.value = res.rows;
  total.value = res.total;
};

/** 查询部门下拉树结构 */
const getDeptTree = async () => {
  const res = await api.deptTreeSelect();
  deptOptions.value = res.data;
  enabledDeptOptions.value = filterDisabledDept(res.data);
};

/** 过滤禁用的部门 */
const filterDisabledDept = (deptList: DeptTreeVO[]) => {
  return deptList.filter((dept) => {
    if (dept.disabled) {
      return false;
    }
    if (dept.children && dept.children.length) {
      dept.children = filterDisabledDept(dept.children);
    }
    return true;
  });
};

/** 节点单击事件 */
const handleNodeClick = (data: DeptVO) => {
  queryParams.value.deptId = data.id;
  handleQuery();
};

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};

/** 重置按钮操作 */
const resetQuery = () => {
  dateRange.value = ["", ""];
  queryFormRef.value?.resetFields();
  queryParams.value.pageNum = 1;
  queryParams.value.deptId = undefined;
  deptTreeRef.value?.setCurrentKey(undefined);
  handleQuery();
};

onMounted(() => {
  getDeptTree(); // 初始化部门数据
  getList(); // 初始化列表数据
  proxy?.getConfigKey("sys.user.initPassword").then((response) => {
    initPassword.value = response.data;
  });
});

async function handleDeptChange(value: number | string) {
  const response = await optionselect(value);
  postOptions.value = response.data;
  form.value.postIds = [];
}

const emit = defineEmits(["selected", "doubleClick"]);

const currentRow = ref<any>();
const handleCurrentChange = (val: any) => {
  currentRow.value = val;
  console.log("-----------------", currentRow.value);
  emit("selected", currentRow.value);
};

const handleDoubleClick = (row: any) => {
  console.log("双击行", row);
  // 双击时也选中该行并触发确认
  currentRow.value = row;
  emit("selected", currentRow.value);
  emit("doubleClick");
};

const pageTableRef = ref<InstanceType<typeof ElTable>>();

</script>
