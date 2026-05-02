# Script para baixar Maven e rodar o projeto HiveX

$MAVEN_VERSION = "3.9.6"
$MAVEN_HOME = "$env:USERPROFILE\.m2\apache-maven-$MAVEN_VERSION"
$MAVEN_ZIP = "$env:TEMP\apache-maven-$MAVEN_VERSION-bin.zip"
$MAVEN_URL = "https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip"

# Detecta JDK automaticamente
$jdkPaths = @(
    "$env:USERPROFILE\.jdks\openjdk-25.0.2-1",
    "$env:USERPROFILE\.jdks\openjdk-25.0.2",
    "$env:USERPROFILE\.jdks\temurin-17",
    "C:\Program Files\Java\jdk-17",
    "C:\Program Files\Eclipse Adoptium\jdk-17*"
)

foreach ($path in $jdkPaths) {
    $resolved = Resolve-Path $path -ErrorAction SilentlyContinue
    if ($resolved) {
        $env:JAVA_HOME = $resolved.Path
        Write-Host "☕ JDK encontrado: $($env:JAVA_HOME)"
        break
    }
}

if (!$env:JAVA_HOME) {
    Write-Host "❌ JDK nao encontrado. Instale o JDK 17+ e tente novamente."
    exit 1
}

# Baixa Maven se necessario
if (!(Test-Path $MAVEN_HOME)) {
    Write-Host "📥 Baixando Maven $MAVEN_VERSION..."
    Invoke-WebRequest -Uri $MAVEN_URL -OutFile $MAVEN_ZIP
    Write-Host "📦 Extraindo Maven..."
    Expand-Archive -Path $MAVEN_ZIP -DestinationPath "$env:USERPROFILE\.m2" -Force
    Remove-Item $MAVEN_ZIP
    Write-Host "✅ Maven instalado!"
}

$env:PATH = "$MAVEN_HOME\bin;$env:PATH"

Write-Host "🚀 Iniciando HiveX Backend..."
& mvn spring-boot:run
