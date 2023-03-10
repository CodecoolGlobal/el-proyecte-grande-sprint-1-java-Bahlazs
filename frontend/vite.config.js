import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/granny': 'http://localhost:8080',
      '/user': 'http://localhost:8080',
    },
  },
})
