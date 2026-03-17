import { defineStore } from 'pinia';
import { ref, computed, watch } from 'vue';

/**
 * 保险缴纳比例配置
 */
export interface InsuranceRateConfig {
  /** 配置ID */
  id: string;
  /** 配置名称 */
  name: string;
  /** 养老保险-个人缴纳比例 */
  pensionPersonalRate: number;
  /** 养老保险-单位缴纳比例 */
  pensionCompanyRate: number;
  /** 医疗保险-个人缴纳比例 */
  medicalPersonalRate: number;
  /** 医疗保险-单位缴纳比例 */
  medicalCompanyRate: number;
  /** 失业保险-个人缴纳比例 */
  unemploymentPersonalRate: number;
  /** 失业保险-单位缴纳比例 */
  unemploymentCompanyRate: number;
}

const STORAGE_KEY = 'insuranceConfig';

/**
 * 从 localStorage 加载配置
 */
const loadFromStorage = () => {
  try {
    const data = localStorage.getItem(STORAGE_KEY);
    if (data) {
      return JSON.parse(data);
    }
  } catch (e) {
    console.error('Failed to load insurance config from localStorage', e);
  }
  return null;
};

/**
 * 保险比例配置 Store
 */
export const useInsuranceConfigStore = defineStore('insuranceConfig', () => {
  // 默认配置列表
  const defaultConfigs: InsuranceRateConfig[] = [
    {
      id: 'default',
      name: '默认配置',
      // 养老保险：个人8%，单位16%
      pensionPersonalRate: 0.08,
      pensionCompanyRate: 0.16,
      // 医疗保险：个人2%，单位8%
      medicalPersonalRate: 0.02,
      medicalCompanyRate: 0.08,
      // 失业保险：个人0.5%，单位0.5%
      unemploymentPersonalRate: 0.005,
      unemploymentCompanyRate: 0.005,
    },
  ];

  // 从 localStorage 加载或使用默认值
  const storedData = loadFromStorage();

  // 配置列表
  const configList = ref<InsuranceRateConfig[]>(storedData?.configList || [...defaultConfigs]);

  // 当前选中的配置ID
  const currentConfigId = ref<string>(storedData?.currentConfigId || defaultConfigs[0].id);

  // 持久化到 localStorage
  const saveToStorage = () => {
    try {
      const data = {
        configList: configList.value,
        currentConfigId: currentConfigId.value,
      };
      localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
    } catch (e) {
      console.error('Failed to save insurance config to localStorage', e);
    }
  };

  // 监听变化并自动保存
  watch(configList, saveToStorage, { deep: true });
  watch(currentConfigId, saveToStorage);

  // 当前选中的配置
  const currentConfig = computed(() => {
    return configList.value.find(c => c.id === currentConfigId.value) || configList.value[0];
  });

  // 获取配置选项列表（用于下拉选择）
  const configOptions = computed(() => {
    return configList.value.map(c => ({
      label: c.name,
      value: c.id,
    }));
  });

  //  getter：获取当前比例配置
  const getInsuranceRate = computed(() => currentConfig.value);

  //  getter：获取养老保险比例
  const getPensionRate = computed(() => ({
    personal: currentConfig.value.pensionPersonalRate,
    company: currentConfig.value.pensionCompanyRate,
  }));

  //  getter：获取医疗保险比例
  const getMedicalRate = computed(() => ({
    personal: currentConfig.value.medicalPersonalRate,
    company: currentConfig.value.medicalCompanyRate,
  }));

  //  getter：获取失业保险比例
  const getUnemploymentRate = computed(() => ({
    personal: currentConfig.value.unemploymentPersonalRate,
    company: currentConfig.value.unemploymentCompanyRate,
  }));

  /**
   * 设置当前配置
   */
  const setCurrentConfig = (id: string) => {
    const config = configList.value.find(c => c.id === id);
    if (config) {
      currentConfigId.value = id;
    }
  };

  /**
   * 添加新配置
   */
  const addConfig = (config: Omit<InsuranceRateConfig, 'id'>) => {
    const newConfig: InsuranceRateConfig = {
      ...config,
      id: `config-${Date.now()}`,
    };
    configList.value.push(newConfig);
    return newConfig.id;
  };

  /**
   * 更新配置
   */
  const updateConfig = (id: string, config: Partial<InsuranceRateConfig>) => {
    const index = configList.value.findIndex(c => c.id === id);
    if (index !== -1) {
      configList.value[index] = { ...configList.value[index], ...config };
    }
  };

  /**
   * 删除配置
   */
  const deleteConfig = (id: string) => {
    // 不能删除最后一个配置
    if (configList.value.length <= 1) {
      return false;
    }
    const index = configList.value.findIndex(c => c.id === id);
    if (index !== -1) {
      configList.value.splice(index, 1);
      // 如果删除的是当前选中的，切换到第一个
      if (currentConfigId.value === id) {
        currentConfigId.value = configList.value[0].id;
      }
      return true;
    }
    return false;
  };

  /**
   * 计算保险金额
   * @param baseAmount 缴纳基数
   * @param type 保险类型
   */
  const calculateInsuranceAmount = (baseAmount: number, type: 'pension' | 'medical' | 'unemployment') => {
    const config = currentConfig.value;
    const base = Number(baseAmount) || 0;
    switch (type) {
      case 'pension':
        return {
          personal: Number((base * config.pensionPersonalRate).toFixed(2)),
          company: Number((base * config.pensionCompanyRate).toFixed(2)),
        };
      case 'medical':
        return {
          personal: Number((base * config.medicalPersonalRate).toFixed(2)),
          company: Number((base * config.medicalCompanyRate).toFixed(2)),
        };
      case 'unemployment':
        return {
          personal: Number((base * config.unemploymentPersonalRate).toFixed(2)),
          company: Number((base * config.unemploymentCompanyRate).toFixed(2)),
        };
      default:
        return { personal: 0, company: 0 };
    }
  };

  return {
    configList,
    currentConfigId,
    currentConfig,
    configOptions,
    getInsuranceRate,
    getPensionRate,
    getMedicalRate,
    getUnemploymentRate,
    setCurrentConfig,
    addConfig,
    updateConfig,
    deleteConfig,
    calculateInsuranceAmount,
  };
});
