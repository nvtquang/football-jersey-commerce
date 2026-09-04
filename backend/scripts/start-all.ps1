$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$services = @(
  @{ Name = "auth-service"; Port = 8081 },
  @{ Name = "catalog-service"; Port = 8082 },
  @{ Name = "content-service"; Port = 8083 },
  @{ Name = "order-service"; Port = 8084 },
  @{ Name = "admin-service"; Port = 8085 },
  @{ Name = "cart-service"; Port = 8086 },
  @{ Name = "api-gateway"; Port = 8080 }
)

$logs = Join-Path $root "logs"
New-Item -ItemType Directory -Force -Path $logs | Out-Null

function Wait-Port {
  param(
    [int] $Port,
    [string] $ServiceName,
    [int] $TimeoutSeconds = 90
  )

  $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
  while ((Get-Date) -lt $deadline) {
    $connection = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
    if ($connection) {
      Write-Host "$ServiceName is ready on port $Port."
      return
    }
    Start-Sleep -Seconds 1
  }

  throw "$ServiceName did not become ready on port $Port within $TimeoutSeconds seconds. Check logs in $logs."
}

function Get-PortOwner {
  param([int] $Port)

  $connection = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1
  if (!$connection) {
    return $null
  }

  Get-CimInstance Win32_Process -Filter "ProcessId = $($connection.OwningProcess)" -ErrorAction SilentlyContinue
}

foreach ($service in $services) {
  $jar = Join-Path $root "$($service.Name)\target\$($service.Name)-1.0.0.jar"
  $outLog = Join-Path $logs "$($service.Name).out.log"
  $errLog = Join-Path $logs "$($service.Name).err.log"

  if (!(Test-Path $jar)) {
    throw "Missing $jar. Run: mvn clean package -DskipTests"
  }

  $owner = Get-PortOwner -Port $service.Port
  if ($owner) {
    if ($owner.CommandLine -and $owner.CommandLine.Contains($jar)) {
      Write-Host "$($service.Name) is already running on port $($service.Port)."
      continue
    }
    throw "Port $($service.Port) is already used by PID $($owner.ProcessId): $($owner.CommandLine). Stop it first or run .\scripts\stop-all.ps1"
  }

  if (Test-Path $outLog) {
    Remove-Item -LiteralPath $outLog -Force
  }
  if (Test-Path $errLog) {
    Remove-Item -LiteralPath $errLog -Force
  }

  Start-Process -FilePath "java" -ArgumentList @("-Ddebug=false", "-jar", "`"$jar`"") -WorkingDirectory $root -RedirectStandardOutput $outLog -RedirectStandardError $errLog -WindowStyle Hidden
  Write-Host "Starting $($service.Name) on port $($service.Port). Log: $outLog"
  try {
    Wait-Port -Port $service.Port -ServiceName $service.Name
  } catch {
    Write-Host "Last lines from ${outLog}:"
    if (Test-Path $outLog) {
      Get-Content $outLog -Tail 80
    }
    Write-Host "Last lines from ${errLog}:"
    if (Test-Path $errLog) {
      Get-Content $errLog -Tail 80
    }
    throw
  }
}

Write-Host "Done. Frontend API base URL: http://localhost:8080/api"
