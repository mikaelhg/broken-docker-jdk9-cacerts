import jks

def gen_jks(pwd):
    data = jks.KeyStore.new('jks', []).saves(pwd)
    text = ''.join(['\\x%02x' % x for x in data])
    print('<%s> %s' % (pwd, text))

gen_jks('')
gen_jks('changeit')