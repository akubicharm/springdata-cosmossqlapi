application.propetiesの値の設定

[application.properties]
```
cosmos.uri=${ACCOUNT_HOST}
cosmos.key=${ACCOUNT_KEY}
cosmos.secondaryKey=${SECONDARY_ACCOUNT_KEY}
```

ACCOUNT_HOST: CosmosDBのURIを設定。例）https://cosmoscosmos.documents.azure.com:443/
ACCOUNT_KEY: CosmosDBのアカウントキーを設定
SECONDARY_ACCOUNT)KEY: CosmosDBのセカンダリアカウントキーを設定

Azure CLIでの確認方法
```
ACCOUNT=cosmoscosmos
RESOURCE_GROUP=mygroup
az cosmosdb keys list --name $ACCOUNT --resource-group $RESOURCE_GROUP


```