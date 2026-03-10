/** @type {import('tailwindcss').Config} */
export default {
  // 配置需要扫描的文件路径（Tailwind 会解析这些文件中的类名）
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  // 添加前缀避免和 Element Plus 样式冲突
  prefix: 'tw-',
  theme: {
    extend: {},
  },
  // 禁用默认样式重置，防止覆盖 Element Plus 样式
  corePlugins: {
    preflight: false,
  },
  plugins: [],
}
