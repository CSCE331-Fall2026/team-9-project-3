import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { GoogleOAuthProvider } from '@react-oauth/google'
import { AuthProvider } from './auth/AuthContext'
import ProtectedRoute from './auth/ProtectedRoute'
import CashierMenu from './pages/CashierMenu'
import CustomerMenu from './pages/CustomerMenu'
import LoginPage from './pages/LoginPage'
import RoleHome from './pages/RoleHome'
import ManagerInventory from './pages/ManagerInventory'
import StatusPage from './pages/StatusPage'
import './App.css'

const googleClientId = import.meta.env.VITE_GOOGLE_CLIENT_ID || ''

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/status" element={<StatusPage />} />
      <Route path="/customer/login" element={<LoginPage customer />} />
      <Route path="/menu" element={(
        <ProtectedRoute roles={['customer']}>
          <CustomerMenu />
        </ProtectedRoute>
      )} />
      <Route
        path="/cashier/menu"
        element={(
          <ProtectedRoute roles={['cashier']}>
            <CashierMenu />
          </ProtectedRoute>
        )}
      />
      <Route
        path="/cashier"
        element={(
          <ProtectedRoute roles={['cashier']}>
            <RoleHome role="cashier" />
          </ProtectedRoute>
        )}
      />
      <Route
        path="/manager"
        element={(
          <ProtectedRoute roles={['manager']}>
            <RoleHome role="manager" />
          </ProtectedRoute>
        )}
      />
      <Route
        path="/manager/inventory"
        element={(
          <ProtectedRoute roles={['manager']}>
            <ManagerInventory />
          </ProtectedRoute>
        )}
      />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  )
}

export default function App() {
  return (
    <GoogleOAuthProvider clientId={googleClientId}>
      <AuthProvider>
        <BrowserRouter>
          <AppRoutes />
        </BrowserRouter>
      </AuthProvider>
    </GoogleOAuthProvider>
  )
}
