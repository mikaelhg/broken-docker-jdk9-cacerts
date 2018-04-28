import jks

data = jks.KeyStore.new('jks', []).saves('')
text = ''.join(['\\x%02x' % x for x in data])

print(text)
