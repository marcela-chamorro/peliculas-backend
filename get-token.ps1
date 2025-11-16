Write-Host "=== VERIFICANDO CONFIGURACIÓN KEYCLOAK ===" -ForegroundColor Cyan

# Verificar que Keycloak esté activo
try {
    $config = Invoke-RestMethod -Uri "http://localhost:9090/realms/cinecloud/.well-known/openid-configuration" -ErrorAction Stop
    Write-Host "✓ Keycloak está activo" -ForegroundColor Green
} catch {
    Write-Host "✗ Keycloak no está disponible: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Verificar endpoints importantes
Write-Host "`n=== ENDPOINTS DISPONIBLES ===" -ForegroundColor Cyan
Write-Host "Token endpoint: $($config.token_endpoint)"
Write-Host "Issuer: $($config.issuer)"

# Probar con diferentes configuraciones de cliente
Write-Host "`n=== PROBANDO DIFERENTES CONFIGURACIONES ===" -ForegroundColor Cyan

$testCases = @(
    @{ClientId = "peliculas-app"; Description = "Cliente principal"},
    @{ClientId = "admin-cli"; Description = "Cliente CLI por defecto"},
    @{ClientId = "realm-management"; Description = "Cliente de gestión del realm"}
)

foreach ($test in $testCases) {
    Write-Host "`nProbando: $($test.Description) (Client: $($test.ClientId))" -ForegroundColor Yellow
    
    $body = @{
        client_id  = $test.ClientId
        username   = "admin"
        password   = "admin123"
        grant_type = "password"
    }
    
    try {
        $response = Invoke-RestMethod -Uri $config.token_endpoint -Method POST -Body $body -ContentType "application/x-www-form-urlencoded" -ErrorAction Stop
        Write-Host "✓ Token obtenido exitosamente!" -ForegroundColor Green
        Write-Host "  Token: $($response.access_token.Substring(0, 50))..." -ForegroundColor Gray
    } catch {
        Write-Host "✗ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}
