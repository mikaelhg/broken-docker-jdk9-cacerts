import jks

data = jks.KeyStore.new('jks', []).saves('')
text = ['\\x%02x' % x for x in data]

print(''.join(text))
