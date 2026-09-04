$ports = 8080, 8081, 8082, 8083, 8084, 8085, 8086

foreach ($port in $ports) {
  $connections = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
  foreach ($connection in $connections) {
    $pidToStop = $connection.OwningProcess
    if ($pidToStop -and $pidToStop -ne $PID) {
      Stop-Process -Id $pidToStop -Force
      Write-Host "Stopped process $pidToStop on port $port."
    }
  }
}

Write-Host "Stopped running TQSport services."
