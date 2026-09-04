$ErrorActionPreference = "Stop"

$baseUrl = "http://localhost:8080/api"

Write-Host "Checking catalog..."
Invoke-RestMethod "$baseUrl/catalog/teams" | Out-Null

Write-Host "Checking products..."
Invoke-RestMethod "$baseUrl/products?size=3" | Out-Null

Write-Host "Checking ADMIN login..."
$adminBody = @{ email = "admin@tqsport.vn"; password = "12345678" } | ConvertTo-Json
$login = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -ContentType "application/json; charset=utf-8" -Body $adminBody

Write-Host "Checking admin dashboard..."
Invoke-RestMethod -Uri "$baseUrl/admin/stats" -Headers @{ Authorization = "Bearer $($login.token)" } | Out-Null

Write-Host "OK: Gateway and core services are running."
